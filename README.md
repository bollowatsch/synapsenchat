# Setup
For the initial setup (installation of JavaFX) please refer to [__Setup of JavaFX__](setup.md#javafx).
# Launch
To test this Chatclient, you need to run two instances. You can either 
1. Run the Client on two separate machines on the same local network or
2. Run two instances on the same machine [_How Can I do this?_](https://stackoverflow.com/questions/41226555/how-do-i-run-the-same-application-twice-in-intellij#:~:text=Click%20Run%2D%3EEdit%20Configurations.,Click%20on%20Apply.)
# Connecting
1. Create a chat as server (**This needs to be done first!**)
   1. Enter your username in the field on the bottom left.
   2. Click on the __+__ icon in the left sidebar 
   3. Enter a chat name, choose the __server__ radio button
   4. Remember the presented IP-address, you will need it for the client
   5. Choose a port between 1024 - 65545 and remember it
   6. Click GO!
2. Create a chat as Client (either on the second instance or second machine)
   1. Enter your username in the field on the bottom left.
   2. Click on the __+__ icon in the left sidebar
   3. Enter a chat name, choose the __client__ radio button
   4. Enter the IP from the server chat instance
   5. Enter the port from the server chat instance
   6. Click GO!
3. Start chatting!