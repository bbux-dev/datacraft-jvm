package org.datacraft

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.datacraft.format.Formatters
import org.datacraft.models.Formatter
import org.datacraft.models.RecordProcessor
import java.io.File

class OutputsTest : StringSpec({

    "should create a SingleFieldOutput with outputKey true" {
        val writer = mockk<WriterInterface>(relaxed = true)
        val outputHandler = Outputs.singleField(writer, true)

        outputHandler.handle("key1", "value1")

        verify { writer.write("key1 -> value1") }
    }

    "should create a SingleFieldOutput with outputKey false" {
        val writer = mockk<WriterInterface>(relaxed = true)
        val outputHandler = Outputs.singleField(writer, false)

        outputHandler.handle("key1", "value1")

        verify { writer.write("value1") }
    }

    "should create a RecordLevelOutput and handle a record" {
        val writer = mockk<WriterInterface>(relaxed = true)
        val processor = mockk<RecordProcessor>()
        val outputHandler = Outputs.recordLevel(processor, writer, 1)

        every { processor.process(any<Map<String, Any>>()) } returns "processed"

        outputHandler.handle("key1", "value1")
        outputHandler.finishedRecord(1L, "group1")

        verify { writer.write("processed") }
    }

    "should create a StdOutWriter and write to stdout" {
        val writer = StdOutWriter()

        // Redirect stdout to capture the output
        val outputStream = java.io.ByteArrayOutputStream()
        val printStream = java.io.PrintStream(outputStream)
        System.setOut(printStream)

        writer.write("test output")

        // Reset stdout
        System.setOut(System.out)

        outputStream.toString().trim() shouldBe "test output"
    }

    "should create a SuppressOutputWriter and not write anything" {
        val writer = SuppressOutputWriter()

        // SuppressOutputWriter does nothing, so there's nothing to verify
        writer.write("test output")
    }

    "should create a SingleFileWriter and write to a file" {
        val tempDir = createTempDir()
        val writer = SingleFileWriter(tempDir.absolutePath, "testfile.txt", true)

        writer.write("test output")

        val writtenFile = File(tempDir, "testfile.txt")
        writtenFile.readText().trim() shouldBe "test output"
    }

    "should create an IncrementingFileWriter and write to multiple files" {
        val tempDir = createTempDir()
        val processor = mockk<RecordProcessor>()
        every { processor.process(any<Map<String, Any>>()) } returns "file1.txt" andThen "file2.txt"
        val writer = IncrementingFileWriter(tempDir.absolutePath, processor)

        writer.write("test output 1")
        writer.write("test output 2")

        val file1 = File(tempDir, "file1.txt")
        val file2 = File(tempDir, "file2.txt")

        file1.readText().trim() shouldBe "test output 1"
        file2.readText().trim() shouldBe "test output 2"
    }

    "should return null processor when neither template nor formatName are supplied" {
        val processor = Outputs.processor()

        processor shouldBe null
    }

    "should throw exception when both template and formatName are supplied" {
        val exception = shouldThrow<IllegalArgumentException> {
            Outputs.processor("template", "formatName")
        }

        exception.message shouldBe "Only one of template or formatName should be supplied"
    }

    "should create processor from template" {
        val template = "{{ name }}.data"
        val processor = mockk<RecordProcessor>()
        mockkObject(TemplateEngines) {
            every { TemplateEngines.string(template) } returns processor

            val result = Outputs.processor(template = template)

            result shouldBe processor
        }
    }

    "should create processor from formatName" {
        val formatName = "json"
        mockkObject(Formatters) {
            every { Formatters.forType(formatName) } returns mockk<Formatter>().also {
                every { it.format(any<Map<String, Any>>()) } returns "formatted"
            }

            val result = Outputs.processor(formatName = formatName)

            result?.process(mapOf("key" to "value")) shouldBe "formatted"
        }
    }

    "should get stdout writer when outdir and suppressOutput are not set" {
        val writer = Outputs.getWriter()

        (writer is StdOutWriter) shouldBe true
    }

    "should get suppress output writer when suppressOutput is true" {
        val writer = Outputs.getWriter(suppressOutput = true)

        (writer is SuppressOutputWriter) shouldBe true
    }

    "should get single file writer when outdir and outfile are set" {
        val writer = Outputs.getWriter(outdir = "testdir", outfile = "testfile.txt", overwrite = true)

        (writer is SingleFileWriter) shouldBe true
    }

    "should get incrementing file writer when outdir is set and outfile is not set" {
        val outdir = "testdir"
        val processor = mockk<RecordProcessor>()
        mockkStatic(::fileNameEngine) {
            every { fileNameEngine(any(), any()) } returns processor

            val writer = Outputs.getWriter(outdir = outdir)

            (writer is IncrementingFileWriter) shouldBe true
        }
    }
})
