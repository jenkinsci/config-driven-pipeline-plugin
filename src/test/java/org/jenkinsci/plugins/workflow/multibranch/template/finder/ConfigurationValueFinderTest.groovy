package org.jenkinsci.plugins.workflow.multibranch.template.finder

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ConfigurationValueFinderTest extends Specification {

    def 'find(#configKey) == #firstValue for config text: #configText'() {
        when:
        String actualValue =
                ConfigurationValueFinder.findFirstConfigurationValue(configText, configKey)

        then:
        actualValue == firstValue

        where:
        firstValue | configKey       | configText
        null       | null            | "test"
        null       | "test1"         | null
        null       | "test1"         | "keynotfound"
        "hi"       | "bare"          | "bare:hi\n test1:second\n test1:third"
        "hi"       | "quotes"        | '"quotes":"hi"\n test1:second\n test1:third'
        "hi"       | "equals_space"  | 'equals_space = "hi"\ntest1 = second\ntest2 = third'
        "hi"       | "equals"        | 'equals="hi"\ntest1=second\ntest2=third'
        "hi"       | "ticks"         | '\'ticks\':\'hi\'\n test1:second\n test1:third'
        "hi"       | "spaces"        | '\'spaces\' :          \'hi\'        \n test1:second\n test1:third'

    }
}
