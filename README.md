# Config-Driven Pipeline Plugin
[![License](https://img.shields.io/github/license/jenkinsci/config-driven-pipeline-plugin.svg)](LICENSE)
[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/config-driven-pipeline.svg)](https://plugins.jenkins.io/config-driven-pipeline)
[![GitHub release](https://img.shields.io/github/release/jenkinsci/config-driven-pipeline-plugin.svg?label=changelog)](https://github.com/jenkinsci/config-driven-pipeline-plugin/releases/latest)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/config-driven-pipeline.svg?color=blue)](https://plugins.jenkins.io/config-driven-pipeline)

## Purpose
Would you like to share `Jenkinsfile` without copy-pasting in git (or other SCMs) but
would also like to be able to have some variance in your `Jenkinsfile` (e.g. configuration 
values such as email address, different unit test scripts, etc...)?

If so, this is the main driver of this plugin. We desired a central git-driven repository
of trusted `Jenkinsfile` templates which are inherently visible, can be contributed to, but 
also allow us the ability to centrally roll out updates and improvements to hundreds of
pipelines at a time. 

This plugin will select a Jenkinsfile based on config in the repository. What this means is 
that you can configure a whole GitHub Organization to use a Jenkinsfile repository and 
different repos can run different Jenkinsfiles in the central Jenkinsfile repo based on 
versioned configuration in the repo (no messing around with job configuration and it's all 
under version control). You simply point your 
[pipeline_template](https://github.com/jenkinsci/config-driven-pipeline-plugin#pipeline_template) 
to the path in the repo. This means you can switch between templates for different branches 
and test out new templates in PRs without having to muck around with job config.

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

## Why would I use this when I can use ${OTHER_SOLUTION}?
### Shared Libraries
[Shared libraries](https://jenkins.io/doc/book/pipeline/shared-libraries/) are fantastic and
are a great way to be able to make your pipeline code testable. However, it was nice to compose
overall stages declaratively in Jenkinsfile and simply let each repo pass in configurable values
such as the unit test command, Docker container to run under, etc...

### Buildpacks
[Buildpacks](https://buildpacks.io/) are also awesome! However, sometimes there aren't quite the
right buildpacks for your needs (and you could also use them within this :smile:).

## Upcoming Additions To This Repo
* Example project configured via [Job DSL Plugin](https://plugins.jenkins.io/job-dsl)
* More configuration information
* Other shinies
* FAQs?
