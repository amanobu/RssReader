RssReader
=========
cd JAVA_HOME/db/bin
ij
connect 'jdbc:derby://localhost:1527/RSSREADER;create=true;user=ユーザ名;password=パスワード'
table作成
cd GLASSFISH_HOME/bin
asadmin
create-jdbc-connection-pool --datasourceclassname org.apache.derby.jdbc.ClientDataSource --restype javax.sql.DataSource --property PortNumber=1527:Password=パスワード:User=ユーザ名:serverName=localhost:DatabaseName=RSSREADER RssReaderPool
ping-connection-pool RssReaderPool
create-jdbc-resource --connectionpoolid RssReaderPool jdbc/rssreader
create-auth-realm --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property jaas-context=jdbcRealm:datasource-jndi=jdbc/rssreader:user-table=USERTBL:user-name-column=USERID:password-column=PW:group-table=GROUPTBL:group-table-user-name-column=USERID:group-name-column=GROUPID:digestrealm-password-enc-algorithm=AES:digest-algorithm=SHA-256:encoding=Hex:charset=UTF-8 jdbc_realm_rssreader