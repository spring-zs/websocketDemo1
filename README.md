# 使用的是ServerEndpoint 实现websocket，其中MoneyServer 中还在注解ServerEndpoint里配置了编解码类
## 1.新建一个springboot工程,添加一个maven依赖
~~~
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
~~~
文件目录结构如下:
![image.png](https://upload-images.jianshu.io/upload_images/17897544-e2aacc2cc6955499.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
## 2. 创建websocket服务端类
MoneyServer.java
~~~
/**
 * @author Page
 * @date 2019-07-02 10:00
 * @description websocket 服务
 */

@Component
@Slf4j
@ServerEndpoint(value = "/money/{userId}", decoders = {
        MessageDecoder.class,}, encoders = {MessageEncoder.class,}, configurator = MoneyRepayConfig.class)
public class MoneyServer {
    private static final Map<String, Session> SESSION_MAP = new HashMap<>();

    @OnOpen
    public void connect(Session session, @PathParam("userId") String userId) {
        // 将session按照房间名来存储，将各个房间的用户隔离
        SESSION_MAP.put(userId, session);
        log.info("websocket成功连接!");
    }

    @OnMessage
    public void repay(RepayReq req, Session session) {
        SESSION_MAP.put(req.getUserId(), session);
        log.info("{}正在还钱:{} 元", req.getName(), req.getMoneyNum());
    }

    public void send(RepayResultRes res) throws IOException, EncodeException {
        if (SESSION_MAP.get(res.getUserId()) == null) {
            log.info("没有找到连接，消息无法推送");
            return;
        }
        SESSION_MAP.get(res.getUserId()).getBasicRemote().sendObject(res);
        log.info("{}成功还钱:{} 元，userId:{},还钱结果:{}", res.getName(),
                res.getMoneyNum(), res.getUserId(), res.isRepayResult()?"还成功" :"失败了");
    }
}
~~~
这里我还添加了一个自定义的解码器和一个编码器,用于解析java对象和前端传来的字符串,以及一个自定义的websocket配置类.不过在这个配置类里边目前什么都没有做.
MessageDecoder.java
~~~
@Slf4j
public class MessageDecoder implements Decoder.Text<RepayReq> {
 
    @Override
    public RepayReq decode(String s) {
        log.info("primal string" + s);
        RepayReq repayReq = null;
        try {
            repayReq = JSONObject.parseObject(s, RepayReq.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return repayReq;
    }
 
    @Override
    public boolean willDecode(String s) {
          
        return (s != null);
    }
 
    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }
 
    @Override
    public void destroy() {
        // do nothing.
    }
}
~~~
MessageEncoder.java
~~~
@Slf4j
public class MessageEncoder implements Encoder.Text<RepayResultRes> {

    @Override
    public String encode(RepayResultRes object) {

        String s = null;
        try {

            s = JSONObject.toJSONString(object);
            log.info("primal: " + object.toString());

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return s;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}
~~~
MoneyRepayConfig.java
~~~
/**
 * @author Page
 * @date 2019-07-05 17:00
 * @description
 */
@Slf4j
public class MoneyRepayConfig extends ServerEndpointConfig.Configurator{
    @Override
    public boolean checkOrigin(String originHeaderValue) {
        log.info("1=========originHeaderValue====={}", originHeaderValue);
        return true;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        log.info("2========{}========={}==",clazz, super.getEndpointInstance(clazz));
        return super.getEndpointInstance(clazz);
    }

    @Override
    public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
        log.info("3========{}====={}======{}", supported, requested, super.getNegotiatedSubprotocol(supported, requested));
        return super.getNegotiatedSubprotocol(supported, requested);
    }

    @Override
    public List<Extension> getNegotiatedExtensions(List<Extension> installed, List<Extension> requested) {
        log.info("4======{}====={}======{}", installed, requested, super.getNegotiatedExtensions(installed, requested));
        return super.getNegotiatedExtensions(installed, requested);
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        log.info("5======{}======{}======{}", sec, request, response);
        super.modifyHandshake(sec, request, response);
    }
}
~~~
ServerEndpointConfig 里有几个可以重写的方法,通过这些方法可以获取websocket的信息以及对连接的一些信息进行修改等.
## 3. 创建WebSocketConfig
通过这个配置类对websocket服务进行发布
~~~
@Configuration
@ServletComponentScan
public class WebSocketConfig extends WsSci {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
~~~
## 4.创建controller
这里我还创建了一个控制器类,用于模拟服务器的响应,发送消息通知客户端.
RepaySuccessController.java
~~~
/**
 * @author Page
 * @Date: 2019-07-02 10:36
 * @Description:
 */

@Controller
@RequestMapping("/money")
public class RepaySuccessController {
    @Resource
    MoneyServer moneyServer;

    @PostMapping("/repaySuccess")
    public void repaySuccess(RepayResultRes req) throws IOException, EncodeException {
        moneyServer.send(req);
    }
}
~~~
## 工程代码已经放在我的github仓库,有需要的可以下载
地址:[欢迎参观](https://github.com/pageyang/websocketTest)
更多关于websocket的内容,可以参考官方文档:
[https://docs.oracle.com/javaee/7/api/javax/websocket/server/package-summary.html](https://docs.oracle.com/javaee/7/api/javax/websocket/server/package-summary.html)
