# ClauDB

ClauDB is a REDIS implementation in Java. At the moment is in development and only implements a small 
subset of commands and features.  The objetive is implement a full functional one-to-one replacement 
for REDIS (2.8 branch).

You will probably wonder why I do this, the answer is I do it Just For Fun.

## Why ClauDB?

Initially I called this project TinyDB, but there's another project with the same name, so, I've
decided to chage to ClaudDB.

Clau is :key: in Valencià, a language spoken in eastern Spain, and ClauDB is a key-value database.

## Implemented commands

- Server
    - FLUSHDB
    - INFO
    - TIME
    - SYNC
    - SLAVEOF
    - ROLE
- Connection
    - ECHO
    - PING
    - QUIT
    - SELECT
- Key
    - DEL
    - EXISTS
    - KEYS
    - RENAME
    - TYPE
    - EXPIRE
    - PERSIST
    - TTL
    - PTTL
- String
    - APPEND
    - DECRBY
    - DECR
    - GET
    - GETSET
    - INCRBY
    - INCR
    - MGET
    - MSET
    - MSETNX
    - SET (with NX, PX, NX and XX options)
    - SETEX
    - SETNX
    - STRLEN
- Hash
    - HDEL
    - HEXISTS
    - HGETALL
    - HGET
    - HKEYS
    - HLEN
    - HSET
    - HVALS
- List
    - LPOP
    - LPUSH
    - LINDEX
    - LLEN
    - LRANGE
    - LSET
    - RPOP
    - RPUSH
- Set
    - SADD
    - SCARD
    - SDIFF
    - SINTER
    - SISMEMBER
    - SMEMBERS
    - SPOP
    - SRANDMEMBER
    - SREM
    - SUNION
- Sorted Set
    - ZADD
    - ZCARD
    - ZRANGEBYSCORE
    - ZRANGE
    - ZREM
    - ZREVRANGE
    - ZINCRBY
- Pub/Sub
    - SUBSCRIBE
    - UNSUBSCRIBE
    - PSUBSCRIBE
    - PUNSUBSCRIBE
    - PUBLISH
- Transactions
    - MULTI
    - EXEC
    - DISCARD
 - Scripting
    - EVAL
    - EVALSHA
    - SCRIPT LOAD
    - SCRIPT EXISTS
    - SCRIPT FLUSH

## Design

ClauDB is implemented using Java8. Is single thread, like REDIS. It uses asynchronous IO 
(netty) and reactive programing paradigm (rxjava).

Requests come from IO threads and enqueues to rxjava single thread scheduler. Then IO thread is free
to process another request. When request is done, the response is sended to client asyncronously. Then,
every request is managed one by one, in a single thread, so there's no concurrency issues to care
about.

## Features

Now only implements a subset of REDIS commands, but is usable.

ClauDB also supports persistence compatible with REDIS, RDB dumps and AOF journal. It can create
compatible RDB files you can load in a REDIS server.

Now ClauDB support master/slave replication, a master can have multiple slaves, but at the moment
slaves can't have slaves.

Also implements partially the Pub/Sub subsystem.

## Performance

Performance is quite good, not as good as REDIS, but it's good enough for Java.

This is ClauDB

    $ redis-benchmark -t set,get -h localhost -p 7081 -n 100000 -q
    SET: 47664.44 requests per second
    GET: 50226.02 requests per second
    
And this is REDIS

    $ redis-benchmark -t set,get -h localhost -p 6379 -n 100000 -q
    SET: 97656.24 requests per second
    GET: 98716.68 requests per second
    
In my laptop (intel core i5, with 4G of RAM)

In the latest version, ClauDB includes an option to use an off heap memory cache. See usage section

## BUILD

You need to clon the repo:

    $ git clone https://github.com/tonivade/claudb.git

ClauDB uses Gradle as building tool, but you don't need Gradle installed, just type:

    $ ./gradlew build

This scripts automatically download Gradle and then runs the tasks. 

Or if you have Gradle installed, just type

    $ gradle build
    
Create all-in-one jar

    $ gradle fatJar

## DOCKER

You can create your own docker images for ClauDB using the provided `Dockerfile`

    $ docker build -t claudb .

And then run the image

    $ docker run -p 7081:7081 claudb

## USAGE

You can start a new server listening in default port 7081.

    $ wget http://repo1.maven.org/maven2/com/github/tonivade/claudb/1.5.0/claudb-1.5.0-all.jar
    $ java -jar claudb-1.5.0-all.jar

or using [jrun](https://github.com/ctrueden/jrun) utility

    $ jrun com.github.tonivade:claudb:com.github.tonivade.claudb.Server
    
Parameters:

    Option        Description
    ------        -----------
    --help        print help
    -V            enable verbose log
    -P            enable persistence (experimental)
    -O            enable off heap memory (experimental)
    -N            enable keyspace notifications (experimental)
    -h <String>   host (default: localhost)
    -p <Integer>  port (default: 7081) 
    
Also you can use inside your project using Maven

    <dependency>
        <groupId>com.github.tonivade</groupId>
        <artifactId>claudb</artifactId>
        <version>1.5.0</version>
    </dependency>
    
Or gradle

    compile 'com.github.tonivade:claudb:1.5.0'

Or embed in your source code

```java
    RespServer server = ClauDB.builder().host("localhost").port(7081).build();
    server.start(); 
```

## TODO

- Ziplist and Maplist encoding not implemented yet.
- Master/Slave replication improvements. Slave with Slaves
- Partitioning?
- Clustering?
- Geo Commands

## Continuous Integration

[![Build Status](https://travis-ci.org/tonivade/claudb.svg?branch=master)](https://travis-ci.org/tonivade/claudb) 
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/63af79474b40420da97b36d02972f302)](https://www.codacy.com/app/tonivade/claudb?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=tonivade/claudb&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/63af79474b40420da97b36d02972f302)](https://www.codacy.com/app/tonivade/claudb?utm_source=github.com&utm_medium=referral&utm_content=tonivade/claudb&utm_campaign=Badge_Coverage)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.tonivade/claudb/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.tonivade/claudb)

## LICENSE

ClauDB is released under MIT License
