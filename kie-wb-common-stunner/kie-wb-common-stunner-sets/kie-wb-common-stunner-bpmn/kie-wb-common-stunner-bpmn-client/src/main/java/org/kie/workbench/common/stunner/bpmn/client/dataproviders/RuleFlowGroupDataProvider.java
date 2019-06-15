/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.bpmn.client.dataproviders;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.workbench.common.stunner.bpmn.forms.dataproviders.ruleflow.RuleFlowGroupDataChangedEvent;
import org.kie.workbench.common.stunner.forms.client.session.StunnerFormsHandler;

import static java.util.Arrays.stream;

@ApplicationScoped
public class RuleFlowGroupDataProvider {

    private final StunnerFormsHandler formsHandler;
    private final List<String> groupNames;

    // CDI proxy.
    public RuleFlowGroupDataProvider() {
        this(null);
    }

    @Inject
    public RuleFlowGroupDataProvider(final StunnerFormsHandler formsHandler) {
        this.formsHandler = formsHandler;
        this.groupNames = new LinkedList<>();
    }

    public List<String> getRuleFlowGroupNames() {
        return groupNames;
    }

    void onRuleFlowGroupDataChanged(final @Observes RuleFlowGroupDataChangedEvent event) {
        setRuleFlowGroupNames(toList(event.getGroupNames()));
    }

    private void setRuleFlowGroupNames(final Collection<String> names) {
        groupNames.clear();
        groupNames.addAll(names);
        formsHandler.refreshCurrentSessionForms();
    }

    private static List<String> toList(final String[] s) {
        return stream(s).collect(Collectors.toList());
    }
}
