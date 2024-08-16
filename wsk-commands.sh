wsk action update clj-get target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.get_users.Main --kind java:8

wsk action update clj-add target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.register_user.Main --kind java:8

#wsk action invoke clj-add --param username mbroughani81 --param password pass1pass2  --result

#wsk action invoke clj-get --result
