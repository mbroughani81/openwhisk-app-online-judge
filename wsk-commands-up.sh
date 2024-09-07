wsk action update clj-get target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.get_users.Main --param-file resources/configs/db.json --kind java:8 --memory 512
wsk action update clj-register target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.register_user.Main --param-file resources/configs/db.json --kind java:8 --memory 512
wsk action update clj-login target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.login_user.Main --param-file resources/configs/db.json --kind java:8 --memory 512
wsk action update clj-eval-code target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.eval_code.Main --param-file resources/configs/db.json --kind java:8 --memory 512
wsk action update clj-create-problem target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.create_problem.Main --param-file resources/configs/db.json --kind java:8 --memory 512
wsk action update clj-create-submit target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.create_submit.Main --param-file resources/configs/db.json --kind java:8 --memory 512
wsk action update clj-eval-submit target/openwhisk-app-0.1.0-SNAPSHOT-standalone.jar --main io.github.mbroughani81.eval_submit.Main --param-file resources/configs/db.json --kind java:8 --memory 512
