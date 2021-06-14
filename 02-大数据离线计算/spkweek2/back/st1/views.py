import json
import os

import jaydebeapi
from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
import yaml
from pyhive import hive
import pandas as pd


def get_jar():
    DIR = os.path.join(os.path.abspath(os.path.dirname(__file__)), 'static/tlib')
    jarFile = []
    for i in os.listdir(DIR):
        jarFile.append(os.path.join(DIR, i))
    return jarFile


def make_conn():
    yaml_file = os.path.join(os.path.abspath(os.path.dirname(__file__)), 'config.yaml')
    with open(yaml_file) as f:
        config_data = yaml.load(f, Loader=yaml.FullLoader)
    jarFile = get_jar()
    global conn
    conn = jaydebeapi.connect(config_data['driver'], config_data['url'],
                              [config_data['user'], config_data['password']], jarFile)


class Get_conn:
    def __init__(self):
        self.conn = self.make_conn()

    def make_conn(self):
        yaml_file = os.path.join(os.path.abspath(os.path.dirname(__file__)), 'config.yaml')
        with open(yaml_file) as f:
            config_data = yaml.load(f, Loader=yaml.FullLoader)
        jarFile = get_jar()

        return jaydebeapi.connect(config_data['driver'], config_data['url'],
                                  [config_data['user'], config_data['password']], jarFile)
    def get_cur(self):
        return self.conn.cursor()

    def __delete__(self, instance):
        self.conn.close()


def add_re(request):
    url = 'jdbc:hive2://bigdata112.depts.bingosoft.net:22112/user08_db'
    user = 'user08'
    password = 'pass@bingo8'
    driver = 'org.apache.hive.jdbc.HiveDriver'

    sql = 'show tables'
    # sql = 'desc formatted t_rk_jbxx'

    jarFile = get_jar()
    conn = jaydebeapi.connect(driver, url, [user, password], jarFile)
    curs = conn.cursor()
    curs.execute(sql)
    result = curs.fetchall()
    print(result)
    curs.close()
    conn.close()
    # return render(request,'show.html',context={'result':str(result)})
    return HttpResponse(str(result))


def get_post(requests):
    yaml_file = os.path.join(os.path.abspath(os.path.dirname(__file__)), 'config.yaml')
    with open(yaml_file) as f:
        config_data = yaml.load(f, Loader=yaml.FullLoader)
    return HttpResponse(str(config_data))


def set_config(request):
    # print(request.GET)
    config_data = {
        'driver': 'org.apache.hive.jdbc.HiveDriver',
        'password': 'pass@bingo5',
        'url': 'jdbc:hive2: // bigdata107.depts.bingosoft.net: 22107 / user05_db',
        'user': 'user05'
    }
    # config_data['password']=request.GET.get('password',config_data['password'])
    # config_data['url']=request.GET.get('url',config_data['url'])
    # config_data['user'] = request.GET.get('user', config_data['user'])
    config_data['password'] = request.GET.get('password')
    config_data['url'] = request.GET.get('url')
    config_data['user'] = request.GET.get('user')
    print(config_data)
    yaml_file = os.path.join(os.path.abspath(os.path.dirname(__file__)), 'config.yaml')
    with open(yaml_file, 'w') as f:
        yaml.dump(config_data, f, Dumper=yaml.Dumper)
    return HttpResponse('done')


print('@' * 100)


def get_tables_list(request):
    conn_maker = Get_conn()
    cur = conn_maker.conn.cursor()
    sql = 'show tables'
    cur.execute(sql)
    query_data = cur.fetchall()
    return_data = {}
    mata_data = []
    for listTab in query_data:
        mata_data.append({'name': listTab[0]})

    return_data['tableListData'] = mata_data
    cur.close()

    return JsonResponse(return_data)


def query_info(request):
    query_sen_sql = request.GET.get('sen')
    # query_sen_sql = 'select * from t_rk_jbxx limit 10'
    exe_sql=''
    for cha in query_sen_sql:
        if cha != '+':
            exe_sql+=cha
        else:
            exe_sql+=' '
    print(exe_sql)
    conn_maker = Get_conn()
    cur = conn_maker.conn.cursor()
    cur.execute(exe_sql)
    res = cur.fetchall()

    table_name = cur.description
    # print(table_name)#获取表头
    table_name_list = []
    for table_tuple in table_name:
        table_name_list.append(table_tuple[0])
    cur.close()
    return_data = {}
    info_list = []
    for info in res:
        info_dict = {}
        for i in range(len(info)):
            info_dict[table_name_list[i]] = info[i]
        info_list.append(info_dict)

    return_data['info_data'] = info_list

    return JsonResponse(return_data)


def get_hive_config_tuple():
    yaml_file = os.path.join(os.path.abspath(os.path.dirname(__file__)), 'config.yaml')
    with open(yaml_file) as f:
        config_data = yaml.load(f, Loader=yaml.FullLoader)
    return config_data


def query_tables_fileds(request):
    json_data = {}
    # sql='select * from t_rk_jbxx limit 1'
    # conn_maker = Get_conn()
    # cur = conn_maker.conn.cursor()
    # cur.execute(sql)
    # res = cur.description
    # field_list=[]
    # for tup in res:
    #     field_list.append(tup[0])
    #
    # return HttpResponse(str(field_list))
    conn_maker=Get_conn()
    with conn_maker.get_cur() as cur:

        cur.execute('show tables')
        tables_tuple_list = cur.fetchall()
        tables_name_list = []
        for tables_tuple in tables_tuple_list:
            tables_name_list.append(tables_tuple[0])
        name_fields_list = []
        for table_name in tables_name_list:
            name_field = {}
            name_field['tableName'] = table_name
            query_field_sql='select * from {} limit 1'.format(table_name)
            print(query_field_sql)
            cur.execute(query_field_sql)
            fields_name_tuple_list=cur.description
            print(str(fields_name_tuple_list))
            fields_name_list=[]
            for name_tuple in fields_name_tuple_list:
                fields_name_list.append(name_tuple[0])
            name_field['fields']=fields_name_list
            # print(name_field['fields'])
            #
            name_fields_list.append(name_field)
    json_data['tableFields']=name_fields_list
    return JsonResponse(json_data)


