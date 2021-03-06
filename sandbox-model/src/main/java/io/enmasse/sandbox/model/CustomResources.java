package io.enmasse.sandbox.model;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;

public class CustomResources {
    private CustomResources() {}

    private static final CustomResourceDefinitionContext sandboxCrdContext = new CustomResourceDefinitionContext.Builder()
            .withGroup("sandbox.enmasse.io")
            .withScope("Cluster")
            .withVersion("v1beta1")
            .withPlural("sandboxtenants")
            .withName("SandboxTenant")
            .build();
    private static final CustomResourceDefinition sandboxCrd = createCustomResource(sandboxCrdContext.getGroup(), sandboxCrdContext.getVersion(), sandboxCrdContext.getName());
    private static final CustomResourceDefinition addressSpaceCrd = createCustomResource("enmasse.io", "v1beta1", "AddressSpace");
    static {
        KubernetesDeserializer.registerCustomKind("sandbox.enmasse.io/v1beta1", "SandboxTenant", SandboxTenant.class);
        KubernetesDeserializer.registerCustomKind("sandbox.enmasse.io/v1beta1", "SandboxTenantList", SandboxTenantList.class);
        KubernetesDeserializer.registerCustomKind("enmasse.io/v1beta1", "AddressSpace", AddressSpace.class);
        KubernetesDeserializer.registerCustomKind("enmasse.io/v1beta1", "AddressSpaceList", AddressSpaceList.class);
    }

    public static CustomResourceDefinitionContext getSandboxCrdContext() {
        return sandboxCrdContext;
    }

    public static CustomResourceDefinition getSandboxCrd() {
        return sandboxCrd;
    }

    public static CustomResourceDefinition getAddressSpaceCrd() {
        return addressSpaceCrd;
    }

    private static CustomResourceDefinition createCustomResource(final String group, final String version, final String kind) {
        String singular = kind.toLowerCase();
        String listKind = kind + "List";
        String plural = singular + "s";
        if (singular.endsWith("s")) {
            plural = singular + "es";
        } else if (singular.endsWith("y")) {
            plural = singular.substring(0, singular.length() - 1) + "ies";
        }
        return new CustomResourceDefinitionBuilder()
                .editOrNewMetadata()
                .withName(plural + "." + group)
                .endMetadata()
                .editOrNewSpec()
                .withGroup(group)
                .withVersion(version)
                .withScope("Cluster")
                .editOrNewNames()
                .withKind(kind)
                .withListKind(listKind)
                .withPlural(plural)
                .withSingular(singular)
                .endNames()
                .endSpec()
                .build();
    }
}
