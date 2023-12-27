package BackEnd.developerIn30Days.ch2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 단일 스레드 버전 서버
 */
public class MentalModServer {

    final private static int PORT = 8080;
    final private static String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    final private static String DYNAMIC = "/dynamic.html";
    final private static byte[] NOT_FOUNT_HTML = "<h1>Not found :(</h1>".getBytes();

    final private static String DIRECTORY = "/src/BackEnd/developerIn30Days/ch2";


    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {  //소켓을 확인하여 요청이 이루어졌는지 확인
            while (true) {
                try {
                    //사용자 요청에 대한 데이터 스트림을 포함할 소켓 인스턴스 반환
                    //새로운 요청이 생성될 때까지 기다리고 handleClient가 완료되어야 새로운 요청을 생성할 수 있음
                    //-> 여러 요청을 한꺼번에 처리하기 어려움
                    Socket client = serverSocket.accept();
                    handleClient(client);
                } catch (Exception err) {
                    err.printStackTrace();
                }

            }
        }
    }

    private static void handleClient(Socket client) throws IOException, InterruptedException {

        Thread.sleep(10000);
        System.out.println(" Current time in seconds: " + Instant.now().getNano());

        //입력 스트림 가져오기
        BufferedReader br = new BufferedReader(
                new InputStreamReader(client.getInputStream())  //바이트 스트림을 받음
        );

        List<String> requestsLines = new ArrayList<>();

        String line;

        //빈 줄을 찾을 때까지 요청의 모든 줄을 읽는다.
        do {
            line = br.readLine();
            requestsLines.add(line);
        } while (line != null && !line.isBlank());

        //요청의 첫 번째 줄에서 요청된 경로를 구문 분석함
        String[] requestLine = requestsLines.get(0).split(" ");
        String path = requestLine[1];

        Path filepath = Paths.get(".", DIRECTORY + path);

        //요청의 경로가 dynamic.html인 경우 오늘 날짜 출력
        if (DYNAMIC.equals(path)) {
            sendResponse(client, "200 OK", "text/html", getDynamicResponse());
        } else if (Files.exists(filepath) && !Files.isDirectory(filepath)) {
            //그렇지 않으면 정적파일 생성
            sendResponse(client, "200 OK", Files.probeContentType(filepath), Files.readAllBytes(filepath));
        } else {
            //그렇지 않으면 오류메시지 출력
            sendResponse(client, "404 Not Found", "text/html", NOT_FOUNT_HTML);
        }


    }

    private static byte[] getDynamicResponse() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String response = String.format(
                "<h1>Dynamic response</h1> Today is %s", sdf.format(cal.getTime())
        );
        return response.getBytes();
    }

    private static void sendResponse(Socket client, String status, String contentType, byte[] content)
            throws IOException {
        String LINE_BREAK = "\r\n";
        OutputStream outputStream = client.getOutputStream();
        outputStream.write(("HTTP/1.1 " + status).getBytes());
        outputStream.write(("ContentType: " + contentType + LINE_BREAK).getBytes());
        outputStream.write(LINE_BREAK.getBytes());
        outputStream.write(content);
        outputStream.write((LINE_BREAK + LINE_BREAK).getBytes());
        outputStream.flush();
        client.close();
    }
}
