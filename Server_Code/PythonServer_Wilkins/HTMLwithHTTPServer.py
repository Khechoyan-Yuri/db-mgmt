'''A simple program to create an html file froma given string,
and call the default web browser to display the file.'''

contents = '''<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta content="text/html; charset=ISO-8859-1"
 http-equiv="content-type">
  <title>Hello</title>
</head>
<body>
Hello, World!
<form name="search" action="/cgi-bin/CGITest.py" method="get">
    <label for="myname">Enter Your Name</label>
    <input id="myname" type="text" name="firstname" value="Nada" />
    <input type="submit">
</form>
</body>
</html>
'''
def server():
    import http.server
    import socketserver
    import ssl
    import os

  #  web_dir = os.path.join(os.path.dirname(__file__), 'web')
   # web_dir = os.getcwd()+'\CGITest.py'
    print(str(web_dir))
    print('\n')

    os.chdir(web_dir)

    PORT = 8000

    Handler = http.server.SimpleHTTPRequestHandler

    httpd = socketserver.TCPServer(("", PORT), Handler)

    httpd.socket = ssl.wrap_socket(httpd.socket, certfile='./server.pem', server_side=True)

    print("serving at port", PORT)
    httpd.serve_forever()

def main():
    browseLocal(contents)

def strToFile(text, filename):
    """Write a file with the given name and the given text."""
    output = open(filename,"w")
    output.write(text)
    output.close()

def browseLocal(webpageText, filename='index.html'):
    '''Start your webbrowser on a local file containing the text
    with given filename.'''
    import webbrowser, os.path
    strToFile(webpageText, filename)

main()
server()
