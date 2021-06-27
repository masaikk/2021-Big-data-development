import logging

from boto3.session import Session
from botocore.exceptions import ClientError
class S3Handler():
    def __init__(self):
        access_key = "29E2BC5B6851CD32568A"
        secret_key = "WzVDQTFCMjlBMjU2NzQ4MTlFMTU3MjdDMzMyQTg1"
        url = "http://scut.depts.bingosoft.net:29997"  # 也可以是自己节点的地址
        session = Session(access_key, secret_key)
        self.s3_client = session.client('s3', endpoint_url=url)

    def download_file(self, bucketName, objectName, fileName):
        """
        下载文件
        :param bucketName: 桶的名称
        :param objectName: 文件的路径
        :param fileName: 下载完成的文件的名称----注意：下载之后的文件默认储存在自己的python工程路径下
        :return:
        """
        self.s3_client.download_file(bucketName, objectName, fileName)

    def upload_file(self, file_name, bucket, object_name=None):
        """
        上传文件
        :param file_name: 需要上传的文件的名称
        :param bucket: S3中桶的名称
        :param object_name: 需要上传到的路径，例如file/localfile/test
        :return:
        """
        if object_name is None:
            object_name = file_name
        try:
            self.s3_client.upload_file(file_name, bucket, object_name, ExtraArgs={'ACL': 'public-read'})
        except ClientError as e:
            logging.error(e)
            return False
        return True

    def list_object(self, bucketName):
        """
        列出当前桶下所有的文件
        :param bucketName:
        :return:
        """
        file_list = []
        response = self.s3_client.list_objects_v2(
            Bucket=bucketName,
            # MaxKeys=1000  # 返回数量，如果为空则为全部
        )
        file_desc = response['Contents']
        for f in file_desc:
            print('file_name:{},file_size:{}'.format(f['Key'], f['Size']))
            file_list.append(f['Key'])
        return file_list
