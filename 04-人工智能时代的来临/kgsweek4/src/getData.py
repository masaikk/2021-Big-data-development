from sklearn.datasets import load_boston
from sklearn.linear_model import LinearRegression
import numpy as np
from sklearn.metrics import mean_squared_error, r2_score

if __name__ == '__main__':
    dataset = load_boston()
    print(dataset.get('feature_names'))
    # 使用第6-8列feature，即AGE(1940年以前建成的自住单位的比例),
    # DIS(距离五个波士顿就业中心的加权距离),RAD(距离高速公路的便利指数)
    X = dataset.data[:, 5:8]
    y = dataset.target

    # 划分训练集和测试集
    X_train = X[:-20]
    X_test = X[-20:]
    y_train = y[:-20]
    y_test = y[-20:]

    regr = LinearRegression()
    regr.fit(X_train, y_train)
    y_test_pred = regr.predict(X_test)

    # print('Coefficients: ', regr.coef_)
    # print('Intercept:', regr.intercept_)
    # print('the model is: y = ', regr.coef_, '* X + ', regr.intercept_)
    # # 均方误差
    # print("Mean squared error: %.2f" % mean_squared_error(y_test, y_test_pred))
    # # r2 score，0,1之间，越接近1说明模型越好，越接近0说明模型越差
    # print('Variance score: %.2f' % r2_score(y_test, y_test_pred))
