package main

object ArgParser {
  type Options = Map[Symbol, Any]
  /**
   * Inspiration: http://stackoverflow.com/questions/2315912/scala-best-way-to-parse-command-line-parameters-cli
   */
  def getOptions(opts: Options, argList: List[String]) : Options = {
    argList match {
      case "--swarm-size" :: value :: rest =>
        getOptions(opts + ('swarmSize -> value.toInt), rest)
      case "--archive-size" :: value :: rest =>
        getOptions(opts + ('archiveSize -> value.toInt), rest)
      case "--max-iterations" :: value :: rest =>
        getOptions(opts + ('maxIterations -> value.toInt), rest)
      case "--psatsim-path" :: value :: rest =>
        getOptions(opts + ('psatsimPath -> value), rest)
      case "--psatsim-name" :: value :: rest =>
        getOptions(opts + ('psatsimName -> value), rest)
      case opt :: rest =>
        println("Ignoring option %s".format(opt))
        getOptions(opts, rest)        
      case Nil => opts
    }
  }
}