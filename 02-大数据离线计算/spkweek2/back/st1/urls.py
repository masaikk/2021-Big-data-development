from django.urls import path, re_path

from st1 import views



urlpatterns = [
    path('are/', views.add_re, name='are'),
    path('getConfig/',views.get_post,name='conf'),
    path('setConfig/',views.set_config,name='setConf'),
    path('query/',views.query_info,name='query'),
    path('fetchTables/',views.get_tables_list,name='tableList'),
    path('tableFields/',views.query_tables_fileds,name='filed')
]
