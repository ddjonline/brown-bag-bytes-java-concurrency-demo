import random
import string
import os

line_length = 64
line_count = 100000
file_name = 'strings.txt'

def generate_strings(file_path, num_strings, str_length):
    """
    Generates a specified number of random strings and writes them to a file.

    Args:
        file_path (str): The path to the output file.
        num_strings (int): The number of strings to generate.
        str_length (int): The length of each string.
    """
    # Using standard ASCII characters (letters and digits)
    chars = string.ascii_letters + string.digits
    
    with open(file_path, 'w') as f:
        for _ in range(num_strings):
            random_string = ''.join(random.choice(chars) for _ in range(str_length))
            f.write(random_string + '\n')

if __name__ == "__main__":
    # Get the directory of the script and create the path for strings.txt
    output_file = os.path.join(os.path.dirname(__file__), file_name)
    generate_strings(output_file, line_count, line_length)
    print(f"Successfully generated {line_count} strings in {output_file}")