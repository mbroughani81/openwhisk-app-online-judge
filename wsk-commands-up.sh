wsk action update clj-get target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.get_users.Main --kind java:8 --memory 512

wsk action update clj-register target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.register_user.Main --kind java:8 --memory 512

wsk action update clj-login target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.login_user.Main --kind java:8 --memory 512

wsk action update clj-eval-code target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.eval_code.Main --kind java:8 --memory 512


wsk action update clj-create-problem target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.create_problem.Main --kind java:8 --memory 512



#wsk action invoke clj-get --result

#wsk action invoke clj-register --param username mbroughani81 --param password pass1pass2  --result

