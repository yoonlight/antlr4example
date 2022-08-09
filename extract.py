from subprocess import Popen, PIPE

def extract_asp(file: str):
    max_path_length = 8
    max_path_width = 2
    lang = "asp"
    jar = "target/antlr4example-1.0-SNAPSHOT.jar"
    command = f'java -cp {jar} com.khubla.antlr4example.Main -l {lang} -pi {file}'
    proc = Popen(command, shell=True, stdout=PIPE, stderr=PIPE)
    script_response = proc.communicate()
    return script_response

if __name__ == '__main__':
    file = "dist/test2.asp"
    print(extract_asp(file)[0].decode('utf-8'))
