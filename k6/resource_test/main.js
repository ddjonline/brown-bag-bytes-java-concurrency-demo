import http from "k6/http";
import { check } from 'k6';
import { SharedArray } from 'k6/data';
import encoding from 'k6/encoding';

export const options = {
  // discardResponseBodies: true, // do not hold the responses in memory to improve performance due to large response payloads
  thresholds: {
    'http_req_failed{request_type:testResource}': ['rate<0.01'], // http errors should be less than 1% for testResource
    'http_req_blocked{request_type:testResource}': ['p(95)<500'], // 95 percentile of the requests must not be blocked more than 0.5 seconds
    'http_req_waiting{request_type:testResource}': ['p(95)<500'], // 95 percentile of the requests must not be waiting more than 0.5 seconds
    'http_req_duration{request_type:testResource}': ['p(95)<10000'], // 95 percentile of requests must complete below 10s for testResource
  },
  scenarios: {
    scenario_process_value: {
      executor: 'per-vu-iterations',
      exec: "testCertificate",
      startTime: '1s', // start right away
      gracefulStop: '1m', // gracefully stop a request after 1 minute
      vus: 100, // virtual users
      iterations: 100, // how many times each virtual user makes the request
      maxDuration: '5m',
      tags: { request_type: 'testCertificate' },
      env: { HTTP_PROTO: 'http', BASE_URL: 'localhost', BASE_PORT: '9080',  URI: '/lookup' }
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
export function testCertificate(data) {

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
    'response body contains last 6 characters': (r) => r.body.includes(lastSixChars),
    'response body exactly matches last 6 characters': (r) => r.body === lastSixChars,
  })
}
