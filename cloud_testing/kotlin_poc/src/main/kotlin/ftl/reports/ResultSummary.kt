package ftl.reports

import ftl.config.FtlConstants
import ftl.config.FtlConstants.indent
import ftl.json.MatrixMap
import ftl.run.TestRunner
import ftl.util.Outcome
import java.nio.file.Files
import java.nio.file.Paths
import java.text.DecimalFormat

/** Used to create result_summary.txt Contains pass/fail metrics **/
object ResultSummary {

    val percentFormat by lazy { DecimalFormat("#0.00") }

    fun run(matrixMap: MatrixMap) {
        val rootFolderPath = Paths.get(FtlConstants.localResultsDir, matrixMap.runPath)

        var total = 0
        var success = 0
        matrixMap.map.values.forEach { result ->
            total += 1
            if (result.outcome == Outcome.success) success += 1
        }
        val failed = total - success
        val successDouble: Double = success / total * 100.0
        val successPercent = percentFormat.format(successDouble)

        val outputPath = Paths.get(rootFolderPath.toString(), "result_summary.txt")
        var outputData = "$success / $total ($successPercent%)\n"
        if (failed > 0) outputData += "$failed matrices failed"

        println("ResultSummary")
        outputData.split("\n").forEach { println(indent + it) }

        Files.write(outputPath, outputData.toByteArray())
    }

    @JvmStatic
    fun main(args: Array<String>) {
        run(TestRunner.lastMatrices())
    }
}
