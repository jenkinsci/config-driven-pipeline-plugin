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
        "value"    | "nonewline"     | "nonewline:value"
        null       | "test1"         | null
        null       | "test1"         | "keynotfound"
        "hi"       | "bare"          | "bare:hi\n test1:second\n test1:third"
        "hi"       | "quotes"        | '"quotes":"hi"\n test1:second\n test1:third'
        "third"    | "last"          | '"quotes":"hi"\n test1:second\n last:third'
        "hi"       | "equals_space"  | 'equals_space = "hi"\ntest1 = second\ntest2 = third'
        "third"    | "last"          | 'equals_space = "hi"\ntest1 = second\nlast = third'
        "hi"       | "equals"        | 'equals="hi"\ntest1=second\ntest2=third'
        "third"    | "last"          | 'equals="hi"\ntest1=second\nlast=third'
        "hi"       | "ticks"         | '\'ticks\':\'hi\'\n test1:second\n test1:third'
        "hi"       | "spaces"        | '\'spaces\' :          \'hi\'        \n test1:second\n test1:third'

    }
}
