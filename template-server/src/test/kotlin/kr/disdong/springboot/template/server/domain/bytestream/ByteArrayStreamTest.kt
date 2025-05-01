package kr.disdong.springboot.template.server.domain.bytestream

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

@DisplayName("파일이나 네트워크 없이 메모리 내에서 스트림처럼 동작해야할 때 유용하다")
class ByteArrayStreamTest {

    @Test
    fun `간단한 테스트`() {
        val original = "Hello, world!"
        val inputBytes = original.toByteArray()

        val inputStream = ByteArrayInputStream(inputBytes)
        val buffer = ByteArray(4)
        while (true) {
            val bytesRead = inputStream.read(buffer)
            if (bytesRead == -1) break
            println(String(buffer, 0, bytesRead))
        }
    }

    @Test
    fun `많은 라이브러리나 함수는 InputStream 이나 OutputStream 형태의 파라미터를 요구하는데 이때 ByteArrayOutputStream 을 사용가능하다`() {
        // 이미지 데이터를 ByteArray로 받은 상황
        val imageBytes: ByteArray = fetchImageFromS3()

        // 이미지 리사이징 라이브러리는 InputStream을 요구
        val resizedImageBytes: ByteArray = ByteArrayOutputStream().use { output ->
            val input = ByteArrayInputStream(imageBytes)
            resizeImage(input, output) // resizeImage(InputStream, OutputStream)
            output.toByteArray()
        }
    }

    private fun fetchImageFromS3(): ByteArray {
        return ByteArray(10)
    }

    private fun resizeImage(input: InputStream, output: OutputStream) {

    }
}