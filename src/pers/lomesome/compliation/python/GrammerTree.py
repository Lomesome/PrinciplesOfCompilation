# -*- coding: utf-8 -*
from pyecharts import options as opts
from pyecharts.charts import Tree
import sys
import demjson

if __name__ == '__main__':
    json = sys.argv[1]
    json = demjson.decode(json)
    data = []
    data.append(json)
    print(data)
    tree=(
        #width="3000px",height="900px"
        Tree( init_opts=opts.InitOpts()).add("", data,orient="TB").set_global_opts(title_opts=opts.TitleOpts(title="语法树"))
    )
    tree.render('./src/pers/lomesome/compliation/python/GrammerTree.html')
    print('语法树生成成功')