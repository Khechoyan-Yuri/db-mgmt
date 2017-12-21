import bcrypt  # Imports the module used for hashing passwords
import os

dir_path = os.path.dirname(os.path.realpath(__file__))
print(str(dir_path))
(dir_path, throwaway) = dir_path.split('\\UserPass')
print(str(dir_path))
dir_path = os.path.join(dir_path, 'cgi-bin')
print(str(dir_path))
#filep = open("pyserv_password.txt", 'r')
fileu = open(dir_path+'\\CGITest.py', 'r')
fusername = fileu.read()
print(str(fusername))
#hashed = eval(filep.read())

'''while bcrypt.hashpw(password, hashed) != hashed and num < 5:
    # Receive password input after failed try
    password = input("Incorrect password. Please try again: ")
    num = num + 1'''


