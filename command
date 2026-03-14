Compile & Run for the Test conectiuon


javac -cp ".;ojdbc17.jar" TestConnection.java
java -cp ".;ojdbc17.jar" TestConnection


Compile and Run for the Bank


javac -cp ".;ojdbc17.jar" Bank.java
java -cp ".;ojdbc17.jar" Bank


complete flow program 


sqlplus scott/sql@localhost:1521/orcl
SELECT table_name FROM user_tables;
SELECT * FROM bank_account;
SELECT * FROM transactions;
EXIT
cd E:\BankProject
javac -cp ".;ojdbc17.jar" *.java
java -cp ".;ojdbc17.jar" Bank