wsk action delete clj-get
wsk action delete clj-register 
wsk action delete clj-login 
#wsk action invoke clj-get --result

#wsk action invoke clj-register --param username mbroughani81 --param password pass1pass2  --result

