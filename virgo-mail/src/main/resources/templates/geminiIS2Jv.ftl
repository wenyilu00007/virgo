<!DOCTYPE html>
<html lang="en">
<meta charset="UTF-8">
<body>
    <h4>
        内部系统调用错误-接口名称: ${interfaceName}
    </h4>
    <div>
        <h5>内部请求地址：${url}</h5>
        <h5>消息ID：${messageId}</h5>
        <p>请求报文：</p>
        <textarea>${requestData}</textarea>
        <p>响应报文：</p>
        <textarea>${responseData}</textarea>
    </div>
</body>
</html>