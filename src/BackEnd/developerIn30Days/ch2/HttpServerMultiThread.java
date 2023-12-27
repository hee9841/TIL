package BackEnd.developerIn30Days.ch2;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpServerMultiThread {

    static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    final private static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    //새 스레드를 만들면 리소스 소모, 무한의 스레드가 생성함
//                    new Thread(new ServerHandler(client)).start();
                    // 그래서 각 요청마다 새 스래드를 생성하는 대신 스레드 풀을 사용해 스레드를 재사용
                    //별도의 스레드에서 ServerHandler의 실행 함수 호출
                    executorService.submit(new ServerHandler(client));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
