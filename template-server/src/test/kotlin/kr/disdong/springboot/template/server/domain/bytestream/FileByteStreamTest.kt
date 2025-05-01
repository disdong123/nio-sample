package kr.disdong.springboot.template.server.domain.bytestream

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting

@DisplayName("""
   Input/OutputStream 은 입출력 데이터를 바이트단위로 읽고 쓰는 가장 기본적인 추상 클래스다.
""")
class FileByteStreamTest {

    @Nested
    @DisplayName("FileInputStream 은 파일을 바이트단위로 읽는다.")
    inner class FileInputStream {
        @Test
        fun `파일이 없으면 FileNotFound 가 발생한다`() {
            assertThrows<FileNotFoundException> {
                FileInputStream("not-found-file.kt")
            }
        }

        @Test
        fun `read 호출 시 1바이트씩 읽으므로 FileTest 를 8번 read 하면 'package '다`() {
            val istream = FileInputStream("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.kt")

            istream.use { it ->
                var packageString = ""
                (0..7).forEach { index ->
                    packageString += it.read().toChar()
                }

                assertEquals(packageString, "package ")
            }
        }

        @Test
        fun `read 값이 -1 이면 더이상 읽을 바이트가 없다는 뜻으로 파일의 끝이다`() {
            val istream = FileInputStream("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.kt")

            istream.use {
                while(true) {
                    val byte = it.read()

                    if (byte == -1) {
                        break;
                    }
                    print(byte)
                }
            }
        }
    }

    @Nested
    @DisplayName("FileOutputStream 은 파일에 데이터를 쓴다.")
    inner class FileOutputStream {
        @Test
        fun `write 호출 시 바이트 단위로 파일에 데이터를 쓴다`() {
            val istream = FileInputStream("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.kt")
            val ostream = FileOutputStream("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.output.kt")

            istream.use { ist ->
                ostream.use { ost ->
                    while (true) {
                        val byte = ist.read()

                        if (byte == -1) {
                            break;
                        }

                        ost.write(byte)
                    }
                }
            }

            val createdFile = Path("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.output.kt")
            assertTrue(Files.exists(createdFile))
            createdFile.deleteExisting()
        }
    }

    @Nested
    @DisplayName("bytearray 로 바꿔서 파일에 쓸 수 있지만 inputstream 이 너무 크면 메모리에 부담이 된다")
    inner class ByteArray {
        @Test
        fun `전부 읽고 한번에 쓰므로 위의 while 로 1바이트씩 읽고 쓰는 코드보다 효율적이다`() {
            val istream = FileInputStream("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.kt")
            val ostream = FileOutputStream("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.output.kt")

            istream.use { ist ->
                ostream.use { ost ->
                    ost.write(ist.readBytes())
                }
            }

            val createdFile = Path("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.output.kt")
            assertTrue(Files.exists(createdFile))
            createdFile.deleteExisting()
        }

        @Test
        fun `파일이 너무 크면 적절한 bytearray size 를 나누어서 하는게 좋다`() {
            val istream = FileInputStream("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.kt")
            val ostream = FileOutputStream("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.output.kt")
            val buffer = ByteArray(8 * 1024) // 8KB, 필요에 따라 조절 가능

            istream.use { input ->
                ostream.use { output ->
                    var bytesRead: Int = input.read(buffer)
                    while (bytesRead != -1) {
                        output.write(buffer, 0, bytesRead)
                        bytesRead = input.read(buffer)
                    }
                }
            }

            val createdFile = Path("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/FileTest.output.kt")
            assertTrue(Files.exists(createdFile))
            createdFile.deleteExisting()
        }
    }
}