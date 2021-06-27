from S3Handler import S3Handler

if __name__ == '__main__':
    s3Obj=S3Handler()
    s3Obj.download_file('hujianqiao02','15.jpg','15.jpg')
