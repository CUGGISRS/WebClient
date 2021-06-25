package com.github.wxiaoqi.security.gate.filter;

import io.netty.buffer.ByteBufAllocator;
import org.bouncycastle.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 获得请求体的过滤器
 */
//@Component
public class CacheBodyGatewayFilter implements Ordered, GlobalFilter {

    public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";

    /**
     * default HttpMessageReader.
     */
    private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        final String method = exchange.getRequest().getMethodValue();
        if ("POST".equals(method)||"PUT".equals(method)) {

            return DataBufferUtils.join(exchange.getRequest().getBody()).map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                return bytes;
            }).flatMap(bodyBytes -> {

                String msg = new String(bodyBytes, StandardCharsets.UTF_8);
                if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(exchange.getRequest().getHeaders().getContentType())){
                    //拼接参数
                    String prefix="Content-Disposition: form-data; name=";
                    String[] ms=msg.split(prefix);
                    StringBuffer params=new StringBuffer();
                    for (int i=1;i<ms.length;i++){
                        String m=ms[i];
                        String[] param=m.split("\n");
                        if(m.indexOf("Content-Type:")>-1){
                            params.append(prefix);
                            params.append(param[0].replaceAll("\r",""));
                            params.append(",");
                            params.append(param[1].replaceAll("\r",""));
                            params.append(";");
                            params.append("\n");
                        }else {
                            params.append(prefix);
                            params.append(param[0].replaceAll("\r",""));
                            params.append(",value=\"");
                            for (int j=1;j<param.length;j++){
                                if(!param[j].trim().equals("")){
                                    params.append(param[j].replaceAll("\r",""));
                                    params.append("\"");
                                    params.append(";");
                                    params.append("\n");
                                     break;
                                }
                            }
                        }
                    }
                    exchange.getAttributes().put(CacheBodyGatewayFilter.CACHE_REQUEST_BODY_OBJECT_KEY, params.toString());
                }else {
                    exchange.getAttributes().put(CacheBodyGatewayFilter.CACHE_REQUEST_BODY_OBJECT_KEY, msg);
                }
                return chain.filter(exchange.mutate().request(generateNewRequest(exchange.getRequest(), bodyBytes)).build());
            });

        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private ServerHttpRequest generateNewRequest(ServerHttpRequest request, byte[] bytes) {
        URI ex = UriComponentsBuilder.fromUri(request.getURI()).build(true).toUri();
        ServerHttpRequest newRequest = request.mutate().uri(ex).build();
        DataBuffer dataBuffer = stringBuffer(bytes);
        Flux<DataBuffer> flux = Flux.just(dataBuffer);
        newRequest = new ServerHttpRequestDecorator(newRequest) {
            @Override
            public Flux<DataBuffer> getBody() {
                return flux;
            }
        };
        return newRequest;
    }

    private DataBuffer stringBuffer(byte[] bytes) {
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        return nettyDataBufferFactory.wrap(bytes);
    }


    /**
     * 用于获取请求参数
     *
     * @param body
     * @return
     */
    private static String toRaw(Flux<DataBuffer> body) {
        AtomicReference<String> rawRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            //  rawRef.set(Strings.fromUTF8ByteArray(bytes));
            rawRef.set(new String(bytes));
        });
        return rawRef.get();
    }

    /**
     * 远程中文乱码（失效中）
     * @param exchange
     * @param chain
     * @param mutatedRequest
     * @return
     */
     /*return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        DataBufferUtils.retain(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux
                                .defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }

                        };
                        //      exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY, toRaw(cachedFlux));


                        final ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                        return cacheBody(mutatedExchange, chain, mutatedRequest);
                 //       return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    });*/
    @SuppressWarnings("unchecked")
    private Mono<Void> cacheBody(ServerWebExchange exchange, GatewayFilterChain chain, ServerHttpRequest mutatedRequest) {
        final HttpHeaders headers = exchange.getRequest().getHeaders();
        final ResolvableType resolvableType;

        if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(headers.getContentType())) {
            resolvableType = ResolvableType.forClassWithGenerics(MultiValueMap.class, String.class, Part.class);
        } else {
            resolvableType = ResolvableType.forClass(String.class);
        }

        return MESSAGE_READERS.stream().filter(reader -> reader.canRead(resolvableType, exchange.getRequest()
                .getHeaders().getContentType())).
                findFirst().
                orElseThrow(() -> new IllegalStateException("no suitable HttpMessageReader.")).
                readMono(resolvableType, exchange.getRequest(), Collections.emptyMap()).
                flatMap(resolvedBody -> {
                    if (resolvedBody instanceof MultiValueMap) {

                        System.out.println(resolvedBody);
                        StringBuffer bodyStr=new StringBuffer();
                        MultiValueMap<String,Part> multiValueMap=(MultiValueMap)resolvedBody;

                        for (String key:multiValueMap.keySet()){
                            Part part=multiValueMap.getFirst(key);
                            //文件属性和普通属性的编码格式不同，一同处理会远程中文乱码
                            if(part.headers().getContentType()!=null){
                                bodyStr.append(new String((part.headers()+"\n"+part.toString()+";"+"\n").getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                            }else {
                                System.out.println("uf1"+part.toString()+"-一起走-");
                            //    System.out.println("gg1"+new String(toRaw(part.content()).getBytes(StandardCharsets.UTF_8),StandardCharsets.UTF_8));
                                bodyStr.append(new String((part.headers()+"\n"+part.toString()+";"+"\n").getBytes(), StandardCharsets.UTF_8));
                            }

                        }
                        exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY,bodyStr.toString());

                    } else {
                        exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY,resolvedBody);
                    }
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }

}
