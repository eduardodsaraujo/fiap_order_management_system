use admin
db.createUser(
    {
        user: "admin",
        pwd: "123456",
        roles: [ "dbOwner" ]
    }
)