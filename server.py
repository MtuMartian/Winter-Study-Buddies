import redis
import ast
import json
import random
import string
import codecs
import unicodedata
import datetime
from uuid import uuid4
from flask import request, abort, Flask
from flask_api import FlaskAPI, status

user_db = redis.StrictRedis(
        host='localhost',
        port=6379,
        db=0
        )

help_posting_db = redis.StrictRedis(
        host='localhost',
        port=6379,
        db=1
        )

message_db = redis.StrictRedis(
        host='localhost',
        port=6379,
        db=2
        )

discussion_db = redis.StrictRedis(
        host='localhost',
        port=6379,
        db=3
        )

key_db = redis.StrictRedis(
        host='localhost',
        port=6379,
        db=4
        )

help_coord_db = redis.StrictRedis(
        host='localhost',
        port=6379,
        db=5
        )

#help_ycoord_db = redis.StrictRedis(
#        host='localhost',
#        port=6379,
#        db=6
#        )

app = Flask(__name__)

def generate_key(length):
    return ''.join(random.choice(string.ascii_letters + string.digits) for _ in range(length))

def convert_form():
    form = {}
    for i in request.form:
        #k = i.encode('ascii', 'ignore')
        k = i.decode('utf-16-be', errors='ignore').encode('ascii')
        v = request.form.get(i).decode('utf-16-be', errors='ignore').encode('ascii')
        # v = request.form.get(i).encode('ascii', 'ignore')
        form[k] = v
    return form

@app.route("/api/validate_key", methods=['POST'])
def validate_key():
    form = request.form
    key = form.get('key', None)
    if(key is None):
        return ("0", status.HTTP_200_OK)
    tmp = key_db.get(key)
    if(tmp is None):
        return ("0", status.HTTP_200_OK)
    return ("0", 202)

@app.route("/api/create_user", methods=['POST'])
def create_user():
    #form = convert_form()
    form = request.form
    username = form.get('username', None)
    password = form.get('password', None)
    email    = form.get('email', None)
    if(username is None or password is None or email is None):
        return ('Missing required form data!', status.HTTP_400_BAD_REQUEST)
    if(len(password) < 8):
        return ('Password is too short!', status.HTTP_400_BAD_REQUEST)
    if(len(user_db.hgetall(username)) != 0):
        return ('Username taken!', status.HTTP_400_BAD_REQUEST)
    year = 2 
    credibility = 2.5
    workedwith  = []
    groups = []
    helpposts = []
    picture   = ''
    major    = ''
    classes = []
    data  = { 'username' : username, 'password' : password, 'email' : email, 'major' : major, 'year' : year, 'credibility' : credibility, 'workedwith' : workedwith, 'groups' : groups, 'classes' : classes, 'helpposts' : helpposts, 'picture' : picture }
    
    user_db.hmset( str(username), data )
    key = generate_key(100)
    while(key_db.get(key) is not None):
        key = generate_key(100)
    key_db.set(key, username)
    key_db.expire(key, 86400)
    return (key, status.HTTP_200_OK)

@app.route("/api/user_info", methods=['POST'])
def user_info():
    form = request.form
    key  = form.get('key', None)
    if(key is None):
        return('Error: invalid key', status.HTTP_400_BAD_REQUEST);
    user = form.get('username')
    info = user_db.hgetall(user)
    info = {key: value for key, value in info.items() if key != 'password'}
    info = json.dumps(info)
    return (info, status.HTTP_200_OK)

@app.route("/api/login", methods=['POST'])
def login():
    form = request.form
    username = form.get('username', None)
    password = form.get('password', None)
    if(username is None or password is None):
        return('Missing username or password!', status.HTTP_400_BAD_REQUEST)
    entry = user_db.hgetall(username)
    if(len(entry) == 0):
        return('Invalid login', status.HTTP_400_BAD_REQUEST)
    if(password != entry['password']):
        return('Invalid password', status.HTTP_400_BAD_REQUEST)
    else:
        key = generate_key(100)
        while(key_db.get(key) is not None):
            key = generate_key(100)
        key_db.set(key, username)
        key_db.expire(key, 86400)
    return(key, status.HTTP_200_OK)

@app.route("/api/get_nearby", methods=['POST'])
def nearby():
    form = request.form
    key  = form.get('key', None)
    tmp  = key_db.get(key)
    if(key is None or tmp is None):
        return('Error: Invalid Key!', status.HTTP_400_BAD_REQUEST)
    xcoord = form.get('xcoord', None)
    ycoord = form.get('ycoord', None)
    if(xcoord is None or ycoord is None):
        return('Error: Missing arguments!', status.HTTP_400_BAD_REQUEST)
    temp_x = help_coord_db.zrangebyscore('xcoord', float(xcoord) - .0005, float(xcoord) + .0005)
    temp_y = help_coord_db.zrangebyscore('ycoord', float(ycoord) - .0005, float(ycoord) + .0005)
    mash_up = list(set(temp_x) & set(temp_y))
    final_list = []
    for entry in mash_up:
        tentry = help_posting_db.hgetall(entry)
        tentry['pid'] = entry
        tentry = json.dumps(tentry)
        final_list.append(tentry)
    return(str(final_list), status.HTTP_200_OK)


@app.route("/api/post_help", methods=['POST'])
def post_help():
    form     = request.form
    key      = form.get('key', None)
    title    = form.get('title', None)
    desp     = form.get('desp', None)
    _class   = form.get('class', None)
    xcoord   = form.get('xcoord', None)
    ycoord   = form.get('ycoord', None)
    username = None
    if(key_db.get(key) is None):
        return('Invalid key: re-login', status.HTTP_405_METHOD_NOT_ALLOWED)
    else:
        username = key_db.get(key)
    if(title is None or desp is None or _class is None or xcoord is None or ycoord is None):
        return('Incomplete help post request!', status.HTTP_400_BAD_REQUEST)
    help_id = help_posting_db.get('id')
    if(help_id == None):
        help_posting_db.set('id', 1)
        help_id = 0
    else:
        help_posting_db.incr('id')

    help_coord_db.zadd('xcoord', xcoord, help_id)
    help_coord_db.zadd('ycoord', ycoord, help_id)
    help_posting_db.hmset(help_id, {'username':username, 'title':title, 'desp':desp, 'class':_class})
    #help_ycoord_db.hset(ycoord, tmpy)
    #users = {username}
    messages = list()
    temp_user = user_db.hgetall(username)
    list(temp_user['helpposts']).append(help_id)
    # Transaction?
    user_db.hmset(username, temp_user)
    discussion_db.hmset(help_id, {'users':set([username]), 'messages':[]})

    return('Success!', status.HTTP_200_OK)

@app.route("/api/new_comment", methods=['POST'])
def comment():
    form = request.form
    key  = form.get('key', None)
    if(key is None):
        return('Error', status.HTTP_404_NOT_FOUND)
    username = key_db.get(key)
    hpost = form.get('hpost', None)
    comm  = form.get('comm', None)
    time  = str(datetime.datetime.now())
    if(hpost is None or comm is None):
        return('Error: bad request', status.HTTP_400_BAD_REQUEST)
    if(len(help_posting_db.hgetall(hpost)) == 0):
        return('Invalid help post!', status.HTTP_404_NOT_FOUND)
    discussion = discussion_db.hgetall(hpost)
    if(len(discussion) == 0):
        return('Invaled discussion', status.HTTP_404_NOT_FOUND)
    comm_id = message_db.get('comm_id')
    if(comm_id is None):
        message_db.set('comm_id', 1)
        comm_id = 0
    else:
        message_db.incr('comm_id')
    message_db.hmset(comm_id, {'time':time, 'username':username, 'comment':comm})
    tmp_msg = ast.literal_eval(discussion['messages'])
    tmp_msg.append(int(comm_id))
    print(type(discussion['users']))
    print(discussion['users'])
    #tmp_users = ast.literal_eval(discussion['users'])
    #tmp_users.add(username)
    tmp_users={}
    discussion_db.hmset(hpost, {'messages':tmp_msg, 'users':tmp_users})
    return("Success!", status.HTTP_200_OK)

@app.route("/api/view_discussion", methods=['POST'])
def view_discussion():
    form = request.form
    key  = form.get('key', None)
    if(key is None):
        return('Error', status.HTTP_404_NOT_FOUND)
    discussion = form.get('hpost')
    dis = discussion_db.hgetall(discussion)
    result = []
    for i in ast.literal_eval(dis['messages']):
        result.append(message_db.hgetall(i))
    return json.dumps(result)

@app.route("/api/user/update_email", methods=['POST'])
def update_email():
    form = request.form
    new_email = form.get('email')
    if(new_email == '' or new_email is None):
        return('Error: Invalid email!', status.HTTP_400_BAD_REQUEST)
    key  = form.get('key', None)
    if(key is None):
        return('Error: Invalid key!', None)
    username = key_db.get(key)
    if(username == '' or username is None):
        return('Error: Invalid key!', None)
    user = user_db.hgetall(username)
    user['email'] = new_email
    user_db.hmset(username, user)
    return('Success!', status.HTTP_200_OK)

@app.route("/api/user/update_major", methods=['POST'])
def update_major():
    form = request.form
    new_major = form.get('major')
    if(new_major == '' or new_major is None):
        return('Error: Invalid major!', status.HTTP_400_BAD_REQUEST)
    key  = form.get('key', None)
    if(key is None):
        return('Error: Invalid key!', None)
    username = key_db.get(key)
    if(username == '' or username is None):
        return('Error: Invalid key!', None)
    user = user_db.hgetall(username)
    user['major'] = new_major
    user_db.hmset(username, user)
    return('Success!', status.HTTP_200_OK)

@app.route("/api/user/update_year", methods=['POST'])
def update_year():
    form = request.form
    new_year = form.get('year')
    if(new_year == '' or new_year is None):
        return('Error: Invalid year!', status.HTTP_400_BAD_REQUEST)
    key  = form.get('key', None)
    if(key is None):
        return('Error: Invalid key!', None)
    username = key_db.get(key)
    if(username == '' or username is None):
        return('Error: Invalid key!', None)
    user = user_db.hgetall(username)
    user['year'] = new_year
    user_db.hmset(username, user)
    return('Success!', status.HTTP_200_OK)

@app.route("/api/user/update_groups", methods=['POST'])
def update_groups():
    form = request.form
    new_groups = form.get('groups')
    if(new_groups == '' or new_groups is None):
        return('Error: Invalid groups!', status.HTTP_400_BAD_REQUEST)
    key  = form.get('key', None)
    if(key is None):
        return('Error: Invalid key!', None)
    username = key_db.get(key)
    if(username == '' or username is None):
        return('Error: Invalid key!', None)
    user = user_db.hgetall(username)
    user['groups'] = new_groups
    user_db.hmset(username, user)
    return('Success!', status.HTTP_200_OK)

@app.route("/api/user/update_classes", methods=['POST'])
def update_classes():
    form = request.form
    new_classes = form.get('classes')
    if(new_classes == '' or new_classes is None):
        return('Error: Invalid classes!', status.HTTP_400_BAD_REQUEST)
    key  = form.get('key', None)
    if(key is None):
        return('Error: Invalid key!', None)
    username = key_db.get(key)
    if(username == '' or username is None):
        return('Error: Invalid key!', None)
    user = user_db.hgetall(username)
    user['classes'] = new_classes
    user_db.hmset(username, user)
    return('Success!', status.HTTP_200_OK)

if __name__ == "__main__":
    app.run(port=8787, host='0.0.0.0', debug=False)
