import bcrypt  # Imports the module used for hashing passwords

filep = open("dec_password.txt", 'w')

# Accepts the input of a new password encoded the byte string
pword = "jman123T*".encode('utf-8')
# Assigns the password hash to the variable with salt
hashed = bcrypt.hashpw(pword, bcrypt.gensalt())
# Writes string version to password file
filep.write(str(hashed))
# Closes file
filep.close()