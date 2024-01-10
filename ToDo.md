# ToDo:
- [ ] CHAT SERIALIZATION NOT WORKING CORRECTLY!!!
- [ ] letting client reconnect to server after connection is lost without having to restart both components!!
- [ ] Terminate Server/Client instances correctly (Press 'X' -> shutdown Instance -> shutdown MessageManager -> serialize all Chats -> System.exit(0))
- [ ] Force input of username or use default value instead
- [ ] Add/Change access modifiers to private where suitable 

# Fixme:
- [ ] Threads don't terminate if Server or Client closes the session!!! (using "terminate" variable to let parent thread know when to exit.)
- [ ] catch error case, if client not created -> don't update view

# BUG:
- [ ] Some...

# DONE:
- [x] Create dedicated Sender and Receiver Classes for MessageManager!
- [x] Cleanup Code in "connection" package
- [x] Server and Client create their own Chat object, saving received and sent messages on their own.
- [x] Server and Client should start in a new Thread with creating a new Chat object.
- [x] A chat should serve as the communication link between the server and the client.
- [x] Chat logic. Each message object should be assigned to a chat.
- [x] Create server or client instances from Chat ---> new Chat(chatName, Server || Client)
- [x] Serialization of Chat object with all sent messages.
- [x] Connect MessageHandling to GUI! (currently using console)
- [x] Multi chat ? (More than one client connecting to the server)
- [x] prevent input of well known ports
- [x] check for correct IP input in GUI
- [x] register enter keystroke for new chat button
- [x] Refactor ChatController to only have atomic methods