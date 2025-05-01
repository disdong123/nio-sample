package kr.disdong.springboot.template.server.domain.bytestream

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.IOException
import java.lang.Integer.*
import java.net.URI
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.spi.FileSystemProvider


@DisplayName("""
    JAVA IO 인 InputStream, OutputStream 은 읽고 쓸 데이터가 있을때까지 블럭킹되는 블럭킹 방식이다.
    JAVA 1.4 에 나온 NIO 는 채널과 버퍼로 블러킹을 최소화한 비동기 입출력을 지원한다
    JAVA 7 에 나온 NIO2 는 기존 File 클래스를 보완하고 파일/디렉토리 조작을 편하게 해준다.
"""
)
class PathTest {

    @Nested
    @DisplayName("""
        File 은 class 고, Path 은 interface 다.
        File 은 os 에 추상화되어 있어 독립적이라 이와 관련 데이터를 가져올 수 없지만 Path 는 os 마다 Path 를 구현한 구현체가 있다.
        따라서 Path 를 생성하면 얘는 파일 시스템의 정보도 함께 들어가있다.
        
        즉, File 은 특정 위치의 파일에 대해 나타내고, Path 는 시스템에 의존적인 파일 경로를 나타낸다.
        
    """)
    inner class Path {
        @Test
        fun `simple test`() {
            // Paths.get("a", "b") 로 하면 파일 시스템에 맞게 윈도우는 \, 다른애는 / 로 된다.
            val path = Paths.get("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream/PathTest.kt")

            val root = Paths.get("src/test/kotlin/kr/disdong/springboot/template/server/domain/bytestream")
            val child = Paths.get("PathTest.kt")

            val pullPath = root.resolve(child)

            assertEquals(pullPath.fileName.toString(), "PathTest.kt")
        }
    }


    @Nested
    @DisplayName(
        """
        FileSystem 은 실제 파일 시스템의 추상화이다.
        FileSystemProvider 는 FileSystem 을 위한 팩토리 클래스로 FileSystem 객체를 생성한다.
        FileStore 은 파일 시스템 내의 파일 저장소의 추상화다. 스토리지의 이름, 타입, 저장 공간 등 정보를 제공한다.
    """
    )
    inner class FileSystem {

        @Test
        fun `여러 파일 시스템을 가지고 있을 수 있고, 내껀 맥이니 MacOS 가 디폴트 파일 시스템이다`() {
            val providers = FileSystemProvider.installedProviders()
            val defaultFs = providers.first().getFileSystem(URI.create("file:///")) // file://: 파일스킴, /: 실제 절대경로

            assertTrue(defaultFs.toString().contains("MacOS"))
            assertEquals(defaultFs, FileSystems.getDefault())

            defaultFs.fileStores.forEach {
                println(it)
            }
        }

        @Nested
        @DisplayName(
            """
        자바는 디렉토리 트리를 DFS 로 탐색한다.
        - find(start, maxDepth, matcher, options)
        - walk(start), walk(start, maxDepth), walk(start, options)
        
        start 는 탐색을 시작할 디렉토리, maxDepth 는 어디까지 탐색할 지.
    """
        )
        inner class Traversal {
            @Test
            fun `find 로 하위의 모든 요소를 탐색하고, matcher 로 어떤 애들만 탐색할 지 정할 수 있다`() {
                val path =
                    Paths.get(URI.create("file:///Users/kimcola/Desktop/develop/spring"))

                val stream = Files.find(path, MAX_VALUE, { path, attrs ->   // attrs: 파일의 속성. lastModifiedTime, creationTime 등
                    attrs.isRegularFile && path.toString().endsWith(".txt")
                })

                println(stream.use { it.count() })
            }

            @Test
            @DisplayName("""
                walk 는 파일탐색 도중에 무언가를 할 수 없지만 아래는 할 수 있다.
               - walkFileTree(start, visitor)
                파일트리를 순회하면서 특정 조건을 만족하는 파일을 탐색/변경/삭제하거나 모든 파일 트리를 경로를 바꾸어 그대로 복사할 때 유용하다.
               
            """)
            fun `FileVisitor 을 구현하면 된다`() {
                val path =
                    Paths.get("src/test/kotlin/kr/disdong/springboot/template/server/domain")

                Files.walkFileTree(path, object : FileVisitor<java.nio.file.Path> {
                    override fun preVisitDirectory(
                        dir: java.nio.file.Path?,
                        attrs: BasicFileAttributes
                    ): FileVisitResult {
                        println("pre... ${dir?.fileName}")
                        return FileVisitResult.CONTINUE
                    }

                    override fun visitFile(file: java.nio.file.Path?, attrs: BasicFileAttributes): FileVisitResult {
                        println("visit... ${file?.fileName}")
                        return FileVisitResult.CONTINUE
                    }

                    override fun visitFileFailed(file: java.nio.file.Path?, exc: IOException): FileVisitResult {
                        println("failed... ${file?.fileName}")
                        return FileVisitResult.CONTINUE
                    }

                    override fun postVisitDirectory(dir: java.nio.file.Path?, exc: IOException?): FileVisitResult {
                        println("post... ${dir?.fileName}")
                        return FileVisitResult.CONTINUE
                    }
                })
            }
        }
    }
}