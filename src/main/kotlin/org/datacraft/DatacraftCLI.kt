package org.datacraft

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int

class DatacraftCLI : CliktCommand(help = "Run datacraft.") {
    private val spec by option("-s", "--spec", help = "Spec to use").file()
    private val inline by option("--inline", help = "Spec as string")
    private val iterations by option("-i", "--iterations", help = "Number of iterations to execute").int().default(100)
    private val outdir by option("-o", "--outdir", help = "Output directory").file()
    private val outfilePrefix by option("-p", "--outfile-prefix", help = "Prefix for output files").default("output")
    private val outfileExtension by option("-ext", "--outfile-extension", help = "Extension for output files").default(".csv")
    private val template by option("-t", "--template", help = "Path to template")
    private val recordsPerFile by option("-r", "--records-per-file", help = "Records per file")
    private val printkey by option("-k", "--printkey", help = "Print field name with value").flag(default = false)
    private val logLevel by option("-l", "--log-level", help = "Logging level").default("info")
    private val format by option("-f", "--format", help = "Formatter for output records")
    private val excludeInternal by option("-x", "--exclude-internal", help = "Exclude internal fields").flag(default = false)
    private val debugSpec by option("--debug-spec", help = "Debug spec after internal reformatting").flag(default = false)
    private val debugSpecYaml by option("--debug-spec-yaml", help = "Debug spec as YAML").flag(default = false)
    private val typeList by option("--type-list", help = "List registered types").flag(default = false)
    private val typeHelp by option("--type-help", help = "Help for registered types").multiple()

    override fun run() {
        // Here you would implement the logic to process the options and execute the corresponding actions.
        println("Spec: $spec")
        println("Inline Spec: $inline")
        println("Iterations: $iterations")
        // Additional processing...
    }
}

fun main(args: Array<String>) = DatacraftCLI().main(args)
