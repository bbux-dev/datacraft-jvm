package org.datacraft

import org.datacraft.format.Formatters
import org.datacraft.models.Formatter
import org.datacraft.models.RecordProcessor
import java.io.File

class Outputs {
    companion object {
        fun singleField(writer: WriterInterface, outputKey: Boolean): OutputHandlerInterface {
            return SingleFieldOutput(writer, outputKey)
        }

        fun recordLevel(
            recordProcessor: RecordProcessor,
            writer: WriterInterface,
            recordsPerFile: Int = 1
        ): OutputHandlerInterface {
            return RecordLevelOutput(recordProcessor, writer, recordsPerFile)
        }

        private fun stdoutWriter(): WriterInterface {
            return StdOutWriter()
        }

        private fun suppressOutputWriter(): WriterInterface {
            return SuppressOutputWriter()
        }

        private fun singleFileWriter(outdir: String, outname: String, overwrite: Boolean): WriterInterface {
            return SingleFileWriter(outdir, outname, overwrite)
        }

        private fun incrementingFileWriter(outdir: String, engine: RecordProcessor): WriterInterface {
            return IncrementingFileWriter(outdir, engine)
        }

        fun processor(template: String? = null, formatName: String? = null): RecordProcessor? {
            require(!(template != null && formatName != null)) { "Only one of template or formatName should be supplied" }

            return when {
                template != null -> {
                    println("Using template: $template")
                    when {
                        File(template).exists() -> TemplateEngines.forFile(template)
                        template.contains("{{") -> TemplateEngines.string(template)
                        else -> throw SpecException("Unable to determine how to handle template $template")
                    }
                }

                formatName != null -> {
                    println("Using $formatName formatter for output")
                    forFormat(formatName)
                }

                else -> null
            }
        }

        fun getWriter(
            outdir: String? = null,
            outfile: String? = null,
            overwrite: Boolean = false,
            outfilePrefix: String = Registries.getDefault("outfile_prefix") as String,
            extension: String = Registries.getDefault("outfile_extension") as String,
            server: Boolean = false,
            suppressOutput: Boolean = false
        ): WriterInterface {
            return if (outdir != null) {
                println("Creating output file writer for dir: $outdir, prefix: $outfilePrefix")
                if (outfile != null) {
                    singleFileWriter(outdir, outfile, overwrite)
                } else {
                    val engine = fileNameEngine(outfilePrefix, extension)
                    incrementingFileWriter(outdir, engine)
                }
            } else {
                if (suppressOutput or server) {
                    suppressOutputWriter()
                } else {
                    println("Writing output to stdout")
                    stdoutWriter()
                }
            }
        }
    }
}

interface WriterInterface {
    fun write(value: String)
}

interface OutputHandlerInterface {
    fun handle(key: String, value: Any)
    fun finishedRecord(iteration: Long? = null, groupName: String? = null, excludeInternal: Boolean = false)
    fun finishedIterations()
}

class SingleFieldOutput(private val writer: WriterInterface, private val outputKey: Boolean) : OutputHandlerInterface {

    override fun handle(key: String, value: Any) {
        if (outputKey) {
            writer.write("$key -> $value")
        } else {
            writer.write(value.toString())
        }
    }

    override fun finishedRecord(iteration: Long?, groupName: String?, excludeInternal: Boolean) {
        // No implementation needed
    }

    override fun finishedIterations() {
        // No implementation needed
    }
}

class RecordLevelOutput(
    private val recordProcessor: RecordProcessor,
    private val writer: WriterInterface,
    private val recordsPerFile: Int
) : OutputHandlerInterface {

    private val current = mutableMapOf<String, Any>()
    private val buffer = mutableListOf<Map<String, Any>>()

    override fun handle(key: String, value: Any) {
        current[key] = value
    }

    override fun finishedRecord(iteration: Long?, groupName: String?, excludeInternal: Boolean) {
        val currentRecord = current.toMutableMap()
        if (!excludeInternal) {
            currentRecord["_internal"] = mapOf(
                "_iteration" to iteration,
                "_field_group" to groupName
            )
        }
        if (recordsPerFile == 1) {
            val processed = recordProcessor.process(currentRecord)
            writer.write(processed)
            current.clear()
        } else {
            buffer.add(currentRecord)
            current.clear()
            if (buffer.size == recordsPerFile) {
                val processed = recordProcessor.process(buffer)
                writer.write(processed)
                buffer.clear()
            }
        }
    }

    override fun finishedIterations() {
        if (buffer.isNotEmpty()) {
            val processed = recordProcessor.process(buffer)
            writer.write(processed)
            buffer.clear()
        }
    }
}

class StdOutWriter : WriterInterface {
    override fun write(value: String) {
        println(value)
    }
}

class SuppressOutputWriter : WriterInterface {
    override fun write(value: String) {
        // No implementation needed
    }
}

class SingleFileWriter(private val outdir: String, private val outname: String, private val overwrite: Boolean) :
    WriterInterface {

    override fun write(value: String) {
        val outfile = File(outdir, outname)
        val mode = if (overwrite) "w" else "a"
        outfile.appendText(value + "\n")
        println("Wrote data to $outfile")
    }
}

class IncrementingFileWriter(private val outdir: String, private val engine: RecordProcessor) : WriterInterface {

    private var count = 0

    init {
        File(outdir).mkdirs()
    }

    override fun write(value: String) {
        val outfile = File(outdir, engine.process(mapOf("count" to count)))
        count++
        outfile.writeText(value + "\n")
        println("Wrote data to ${outfile.path.replace("/", File.separator)}")
    }
}

class FormatProcessor(private val formatter: Formatter) : RecordProcessor {

    override fun process(record: Map<String, Any>): String {
        return formatter.format(record)
    }

    override fun process(records: List<Map<String, Any>>): String {
        return formatter.format(records)
    }
}

fun forFormat(key: String): RecordProcessor {
    val formatter: Formatter =
        Formatters.forType(key) ?: throw SpecException("Unable to load RecordProcessor for format $key")
    return FormatProcessor(formatter)
}



fun fileNameEngine(outfilePrefix: String, extension: String): RecordProcessor {
    return FileNameProcessor(outfilePrefix, extension)
}

class FileNameProcessor(private val prefix: String, private val extension: String) : RecordProcessor {
    override fun process(record: Map<String, Any>): String {
        return "$prefix${record["count"]}.$extension"
    }

    override fun process(records: List<Map<String, Any>>): String {
        // not implemented
        throw NotImplementedError()
    }

}
