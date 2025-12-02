import http from "k6/http";
import { check } from 'k6';
import { SharedArray } from 'k6/data';
import encoding from 'k6/encoding';

export const options = {
  // discardResponseBodies: true, // do not hold the responses in memory to improve performance due to large response payloads
  thresholds: {
    'http_req_failed{request_type:sb_basic}': ['rate<0.01'], // http errors should be less than 1% for sb_basic
    'http_req_blocked{request_type:sb_basic}': ['p(95)<500'], // 95 percentile of the requests must not be blocked more than 0.5 seconds
    'http_req_waiting{request_type:sb_basic}': ['p(95)<500'], // 95 percentile of the requests must not be waiting more than 0.5 seconds
    'http_req_duration{request_type:sb_basic}': ['p(95)<10000'], // 95 percentile of requests must complete below 10s for sb_basic

    'http_req_failed{request_type:sb_basic_vt}': ['rate<0.01'], // http errors should be less than 1% for sb_basic_vt
    'http_req_blocked{request_type:sb_basic_vt}': ['p(95)<500'], // 95 percentile of the requests must not be blocked more than 0.5 seconds
    'http_req_waiting{request_type:sb_basic_vt}': ['p(95)<500'], // 95 percentile of the requests must not be waiting more than 0.5 seconds
    'http_req_duration{request_type:sb_basic_vt}': ['p(95)<10000'], // 95 percentile of requests must complete below 10s for sb_basic_vt

    'http_req_failed{request_type:sb_async}': ['rate<0.01'], // http errors should be less than 1% for sb_async
    'http_req_blocked{request_type:sb_async}': ['p(95)<500'], // 95 percentile of the requests must not be blocked more than 0.5 seconds
    'http_req_waiting{request_type:sb_async}': ['p(95)<500'], // 95 percentile of the requests must not be waiting more than 0.5 seconds
    'http_req_duration{request_type:sb_async}': ['p(95)<10000'], // 95 percentile of requests must complete below 10s for sb_async
  },
  scenarios: {
    scenario_process_value_sb_basic: {
      executor: 'per-vu-iterations',
      exec: "testLookup",
      startTime: '1s', // start right away
      gracefulStop: '1m', // gracefully stop a request after 1 minute
      vus: 10, // virtual users
      iterations: 100, // how many times each virtual user makes the request
      maxDuration: '5m',
      tags: { request_type: 'sb_basic' },
      env: { HTTP_PROTO: 'http', BASE_URL: 'localhost', BASE_PORT: '8081',  URI: '/lookup' }
    },
    scenario_process_value_sb_basic_vt: {
      executor: 'per-vu-iterations',
      exec: "testLookup",
      startTime: '1s', // start right away
      gracefulStop: '1m', // gracefully stop a request after 1 minute
      vus: 10, // virtual users
      iterations: 100, // how many times each virtual user makes the request
      maxDuration: '5m',
      tags: { request_type: 'sb_basic_vt' },
      env: { HTTP_PROTO: 'http', BASE_URL: 'localhost', BASE_PORT: '8082',  URI: '/lookup' }
    },
    scenario_process_value_sb_async: {
      executor: 'per-vu-iterations',
      exec: "testLookup",
      startTime: '1s', // start right away
      gracefulStop: '1m', // gracefully stop a request after 1 minute
      vus: 10, // virtual users
      iterations: 100, // how many times each virtual user makes the request
      maxDuration: '5m',
      tags: { request_type: 'sb_async' },
      env: { HTTP_PROTO: 'http', BASE_URL: 'localhost', BASE_PORT: '8083',  URI: '/lookup' }
    },
  },
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)', 'count'],
}

// =========================================================================
// --- init lifecycle (called before init phase setup function)
// =========================================================================
// load in the values list as JSON/JavaScript object
const keyValues = new SharedArray('values', function () {
  return JSON.parse(open('../data/data.json')).values;
});

// =========================================================================
// --- scenario functions (see the "exec" for a given scenario)
// =========================================================================
export function testLookup(data) {

  const OPTIONS = {
    headers: {
      Accept: "*/*"
    }
  };

  const randomValue = keyValues[Math.floor(Math.random() * keyValues.length)];
  const lastSixChars = randomValue.slice(-6);

  const response = http.get(`${__ENV.HTTP_PROTO}://${__ENV.BASE_URL}:${__ENV.BASE_PORT}${__ENV.URI}/${randomValue}`, OPTIONS);
  check(response, {
    'lookup response status is 200 or 404': (r) => r.status === 200 || r.status === 404,
    'response body matches reconstructed value': (r) => r.body === randomValue,
  })
}
