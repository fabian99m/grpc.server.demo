package grpc.server.service;

import com.google.rpc.Code;
import com.google.rpc.Status;
import grpc.server.proto.HelloWorldReply;
import grpc.server.proto.HelloWorldRequest;
import grpc.server.proto.ReactorMyServiceGrpc.MyServiceImplBase;
import io.grpc.StatusException;
import io.grpc.protobuf.StatusProto;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService(interceptors = {LogInterceptor.class})
public class MyServiceImpl extends MyServiceImplBase {

    @Override
    public Mono<HelloWorldReply> sayHello(HelloWorldRequest request) {
        if (request.getName().isBlank()) {
            return Mono.error(errorFrom(Code.INVALID_ARGUMENT, "Parameter name invalid..."));
        }

        return Mono.just(request).map(this::response);
    }

    private HelloWorldReply response(HelloWorldRequest req) {
        return HelloWorldReply.newBuilder()
                .setMessage(String.format("Hello World to : %s", req.getName()))
                .build();
    }

    private StatusException errorFrom(Code code, String message) {
        var status = Status.newBuilder()
                .setCode(code.getNumber())
                .setMessage(message)
                .build();
        return StatusProto.toStatusException(status);
    }
}