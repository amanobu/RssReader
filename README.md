RssReader
=========

##概要
RssReader

##目的
Java EE 7の機能の学習のため
出来るだけJava EE 7の機能を利用しつつ実現します

##使っている技術/ツール/ライブラリ
* CDI
* EJB
* JSF
* JAX-RS
* Concurrency Utilities for Java EE
* JPA
* JTA
* Bean Validation
* JDBC Realm
* Java DB
* PrimeFaces
* Maven
* ROME

##必要環境
* JAVA 8
* Java EE 7 対応のサーバ
    * 動作確認はGlassFish 4で動きを確認しています
* DB
    * JAVA DBを使っています

##環境構築
1. javaは適当にinstall.JAVA_HOMEに入れたとする
1. GlassFishも適当にinstall.GLASSFISH_HOMEに入れたとする
1. DBの準備
    * cd JAVA_HOME/db/bin
    * ij
    * DBを作成します  
	connect 'jdbc:derby://localhost:1527/RSSREADER;create=true;user=ユーザ名;password=パスワード'
    * tableの作成  
	createtable.sqlの中身を実行する
1. GlassFishの準備
    * 管理ツール起動  
		cd GLASSFISH_HOME/bin  
		asadmin
    * JDBCリソースプールの作成  
       create-jdbc-connection-pool --datasourceclassname org.apache.derby.jdbc.ClientDataSource --restype javax.sql.DataSource --property PortNumber=1527:Password=パスワード:User=ユーザ名:serverName=localhost:DatabaseName=RSSREADER RssReaderPool
    * DBに接続出来るか動作確認  
	  ping-connection-pool RssReaderPool
    * JDBCリソースの作成  
	  create-jdbc-resource --connectionpoolid RssReaderPool jdbc/rssreader
    * Form認証用のJDBC Realmの作成  
	  create-auth-realm --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property jaas-context=jdbcRealm:datasource-jndi=jdbc/rssreader:user-table=USERTBL:user-name-column=USERID:password-column=PW:group-table=GROUPTBL:group-table-user-name-column=USERID:group-name-column=GROUPID:digestrealm-password-enc-algorithm=AES:digest-algorithm=SHA-256:encoding=Hex:charset=UTF-8 jdbc_realm_rssreader


