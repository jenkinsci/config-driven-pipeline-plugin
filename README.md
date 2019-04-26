# Config-Driven Pipeline Plugin

## Purpose
Would you like to share `Jenkinsfile` without copy-pasting in git (or other SCMs) but
would also like to be able to have some variance in your `Jenkinsfile` (e.g. configuration 
values such as email address, different unit test scripts, etc...)?

If so, this is the main driver of this plugin. We desired a central git-driven repository
of trusted `Jenkinsfile` templates which are inherently visible, can be contributed to, but 
also allow us the ability to centrally roll out updates and improvements to hundreds of
pipelines at a time. 

## Setup
This plugin provides you with a new Project Recognizer that you can use with any 
[Multibranch Pipeline type](https://jenkins.io/doc/book/pipeline/multibranch/#creating-a-multibranch-pipeline)
such as a GitHub Organization or Multibranch Pipeline. 

![Config-Driven Pipeline Project Recognizer](/images/config-driven-pipeline-project-recognizer.png)

You'll simply set the `Config File Path` to a location where you expect the config file to reside in 
the repositories (traditionally at the root of the repo).

### pipeline_template
The `pipeline_template` configuration key is reserved for finding the `Jenkinsfile` template you'd
like to use out of the centralized `Jenkinsfile` repo. 

### Config File Format - Most Any!
The plugin itself is only going to search for a `pipeline_template` key/value in your Yaml, JSON, 
Java property file (and likely some others). This logic is in the `ConfigurationValueFinder` and
we'd be happy to entertain additions to expand compatibility.  We recommend using the 
[Pipeline Utility Steps Plugin](https://plugins.jenkins.io/pipeline-utility-steps) to parse your
config but you're free to implement and validate this however you'd like in your `Jenkinsfile` 
templates.

## Config File Contents Available for Parsing!
The plugin places the contents of the config file in the `PIPELINE_CONFIG` environment variable so
that you don't have to read the file again.

## Upcoming Additions To This Repo
* Example project configured via [Job DSL Plugin](https://plugins.jenkins.io/job-dsl)
* More configuration information
* Other shinies
* FAQs?
