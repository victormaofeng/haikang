"""
工具模块
"""


# 拼接文件路径
def concat_path(parent: str, child: str):
    # linux格式
    if parent.find("/") > -1:
        if parent.endswith("/") and child.startswith("/"):
            return parent + child.replace('/', '', 1)
        elif (not parent.endswith("/")) and not child.startswith("/"):
            return parent + "/" + child
        else:
            return parent + child
    # windows格式
    else:
        if parent.endswith("\\") and child.startswith("/"):
            return parent + child.replace('/', '', 1).replace('/', '\\')
        elif (not parent.endswith('\\')) and not child.startswith("/"):
            return parent + "\\" + child.replace('/', '\\')
        else:
            return parent + child.replace('/', '\\')


# 根据算法id获取处理对象
def process(algo_id: int):

    return

# print(concat_path("C:\\a\\", "/a/s/1.flv"))
# print(concat_path("C:\\a\\", "a/s/1.flv"))
# print(concat_path("/a/b/c", "/a/s/1.flv"))
# print(concat_path("/a/b/c/", "/a/s/1.flv"))
