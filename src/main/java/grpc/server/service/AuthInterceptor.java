package grpc.server.service;

import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
//import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

//@GrpcGlobalServerInterceptor
public class AuthInterceptor implements ServerInterceptor {

    private static final String AUTH_HEADER = "Authorization";

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        var authHeader = metadata.get(Key.of(AUTH_HEADER, Metadata.ASCII_STRING_MARSHALLER));

        if (authHeader == null || authHeader.isBlank() || !authHeader.equals("test")) {
            serverCall.close(Status.UNAUTHENTICATED, new Metadata());
        }
        return serverCallHandler.startCall(serverCall, metadata);
    }
}