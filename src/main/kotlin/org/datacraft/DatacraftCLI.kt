package org.datacraft

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import com.google.gson.Gson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class DatacraftCLI : CliktCommand(help = "Run datacraft.") {
    private val spec by option("-s", "--spec", help = "Spec to use").file()
    private val inline by option("--inline", help = "Spec as string")
    private val iterations by option("-i", "--iterations", help = "Number of iterations to execute").long().default(100)
    private val outdir by option("-o", "--outdir", help = "Output directory").file()
    private val outfilePrefix by option("-p", "--outfile-prefix", help = "Prefix for output files").default("output")
    private val outfileExtension by option("-ext", "--outfile-extension", help = "Extension for output files").default(".csv")
    private val template by option("-t", "--template", help = "Path to template")
    private val recordsPerFile by option("-r", "--records-per-file", help = "Records per file").int()
    private val printkey by option("-k", "--printkey", help = "Print field name with value").flag(default = false)
    private val logLevel by option("-l", "--log-level", help = "Logging level").default("info")
    private val format by option("-f", "--format", help = "Formatter for output records")
    private val excludeInternal by option("-x", "--exclude-internal", help = "Exclude internal fields").flag(default = false)
    private val debugSpec by option("--debug-spec", help = "Debug spec after internal reformatting").flag(default = false)
    private val debugSpecYaml by option("--debug-spec-yaml", help = "Debug spec as YAML").flag(default = false)
    private val typeList by option("--type-list", help = "List registered types").flag(default = false)
    private val typeHelp by option("--type-help", help = "Help for registered types").multiple()

    override fun run() {
        // these bypass spec loading
        if (typeList) {
            val types = Loaders.configuredTypes()
            println(types)
            return
        }
        val spec : DataSpec = loadSpec(spec, inline)
        val records = spec.entries(iterations)
        println(Gson().toJson(records));
    }

    fun loadSpec(specPath: File?, inline: String?, templateVars: Map<String, String> = emptyMap()): DataSpec {
        if (specPath == null && inline == null) {
            throw SpecException("One of --spec <spec path> or --inline \"<spec string>\" must be specified")
        }
        if (specPath != null && inline != null) {
            throw SpecException("Only one of --spec <spec path> or --inline \"<spec string>\" must be specified")
        }

        return if (inline != null) {
            parseSpecString(inline)
        } else {
            loadJsonOrYaml(specPath!!)
        }
    }

    fun parseSpecString(inline: String): DataSpec {
        return DataSpec.parseString(inline)
    }

    fun loadJsonOrYaml(specPath: File): DataSpec {
        return DataSpec.parseString(specPath.readText(Charsets.UTF_8))
    }
}

fun main(args: Array<String>) = DatacraftCLI().main(args)
