
So far there are 7 handler tempate classes. These classes are to be used as a template to create 
new handlers. At a minimum, all that has to be done to create a new handler is to copy the add,
get, put, delete classes to the new destination, and one would have to replace the work 'type' 
with the actual object being serviced (ie replace type with task).

app/src/main/java/api/v1/TypeRequestHandler.java
app/src/main/java/api/v1/repo/TypeRepository.java
app/src/main/java/api/v1/model/Type.java
app/src/main/java/api/v1/handlerTemplate/AddType.java
app/src/main/java/api/v1/handlerTemplate/DeleteType.java
app/src/main/java/api/v1/handlerTemplate/GetType.java
app/src/main/java/api/v1/handlerTemplate/PutType.java
