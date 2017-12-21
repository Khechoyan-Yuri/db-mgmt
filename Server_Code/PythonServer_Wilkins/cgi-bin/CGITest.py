
# -*- coding: UTF-8 -*-

# enable debugging
import cgitb
import cgi
import os
import bcrypt  # Imports the module used for hashing passwords

cgitb.enable()

print ("Content-type: text/html\n")

dir_path = os.path.dirname(os.path.realpath(__file__))

(dir_path, throwaway) = dir_path.split('\\cgi-bin')

dir_path = os.path.join(dir_path, 'UserPass')


filep = open(dir_path+"\pyserv_password.txt", 'r')
fileu = open(dir_path+"\pyserv_user.txt", 'r')
fusername = fileu.read()
hashed = eval(filep.read())
filep.close()
fileu.close()

formData = cgi.FieldStorage()
name = formData.getvalue('user')
password = formData.getvalue('pass')

if bcrypt.hashpw(bytes(password, 'utf-8'), hashed) == hashed and name == fusername:

    print ("<html>")
    print ("<body>")
    print ("<h1>Hello %s</h1>" % name)
    print ("You have successfully logged in.")
    print ("</pre>")
    print ("</body>")
    print ("</html>")
else:
    print("<html>")
    print("<body>")
    print("<h1>Howdy Stranger</h1>")
    print("You cannot login.")
    print("</pre>")
    print("</body>")
    print("</html>")
