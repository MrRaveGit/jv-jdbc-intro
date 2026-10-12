mvnw clean package## Common mistakes

* Try to avoid code duplication. Especially, when you are working with ResultSet.
  Move retrieving data from ResultSet into Entity object to a separate private method.

* Don't make `books.title` UNIQUE. This is not required in this task.

* When creating a table in MySQL, use `bigint` column type for storing id.

* Use `if` or `while` with `resultSet.next()`. Don't do it without checking,
  because the result can be `null` and you will get a NPE after trying to get a value from `resultSet`.
  
* Use `PreparedStatement` over `Statement`, even for a static query with no parameters in `findAll()` method. It's the best practice, and it's slightly faster.

* Column naming (`books.` is a table name in these examples):

Wrong: `books.bookTitle`, `books.BOOK_TITLE`

Good: `books.title`

* Use `Statement.RETURN_GENERATED_KEYS` only in `create` statement, it's not needed in other methods.

* Be attentive with:

    - Bad practice:
        ```java
            String bookTitle = resultSet.getString("title");
            Long bookId = resultSet.getLong("id"); // return '0' if data is absent.
        ``` 
    - Good practice: 
        ```java
            String title = resultSet.getString("title");
            Long id = resultSet.getObject("id", Long.class); // return 'null' if data is absent.
        ```

* Don't return `true` all the time in the method `delete`.
  Let's return boolean value depending on `preparedStatement.executeUpdate()` [result](https://docs.oracle.com/javase/7/docs/api/java/sql/Statement.html#executeUpdate(java.lang.String)).

  Example:
  ```java
        ...
        int updatedRows = preparedStatement.executeUpdate();
        return updatedRows > 0;
  ```
  Don't use `updatedRows == 1` - the example above, that uses comparison with `0`, is more flexible.

* Remember about SQL style: use uppercase for SQL keywords in your queries.

    - Bad practice:
        ```sql  
        insert into books (..) VALUES (..);    
        ``` 
    - Good practice: 
        ```sql
        INSERT INTO books (..) VALUES (..);
        ```   
* Let's save each query in a separate variable.
    - Bad practice:
        ```java
            public List<Book> findAll() {
                try (Connection connection = ConnectionUtil.getConnection()
                    PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM books")) { // it's bad
                    ...
                } catch (SQLException ex) {
                    ...
                }
            }
        ``` 
    - Good practice: 
        ```java
            public List<Book> findAll() {
                String query = "SELECT * FROM books"; // it's good
                try (Connection connection = ConnectionUtil.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ...
                } catch (SQLException ex) {
                    ...
                }
            }
        ```

* Best practices with closing Connections and/or PreparedStatements
    - You have to close the PreparedStatement after you're done with it and before you create a new one on the same connection.
    - Generally, when you close the connection it automatically closes the statement.
      But, for example, if for some reason you are using a connection pool (we are not using it now),
      and you call `connection.close()`, the connection will be returned to the pool,
      and the Statement will never be closed. Then you will run into many new problems!
      In any case, it's a good practice to always close and Statement explicitly and not to rely on `connection.close()`.
    - So let's close PreparedStatement as well as Connection (use try with resources for that).


* Use informative messages for exceptions.
    - Bad practice:
        ```java
            throw new DataProcessingException("Can't get a book", e);
        ``` 
    - Good practice: 
        ```java
            throw new DataProcessingException("Can't get a book by id " + id, e);
            throw new DataProcessingException("Can't save a book " + book, e);
        ``` 

* Don't use schema's name in your queries, because you are configuring it while establishing a connection with DB.

    - Bad practice:
        ```sql  
        SELECT * FROM schemaname.books WHERE id = 1;                     
        ``` 
    - Good practice: 
        ```sql
        SELECT * FROM books WHERE id = 1;
        ```         
* When you convert `ResultSet` to `Book` better create an object using setters or constructor but not both of them, because it's not consistent to use both ways of initialization of object.

  ```java
       Long id = rs.getObject(1, Long.class);
       String title = rs.getString(2);
  ```
       
    - Bad practice:
        ```java
        Book book = new Book(title);
        book.setId(id); 
        ``` 
    - Good practice: 
        ```java
        Book book = new Book(id, title);
        
        // or
        
        Book book = new Book();
        book.setId(id);  
        book.setTitle(title);
        ```  
    
