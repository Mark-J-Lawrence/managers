package dev.galasa.common.artifact.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.annotations.Component;

import dev.galasa.ManagerException;
import dev.galasa.common.artifact.ArtifactManager;
import dev.galasa.common.artifact.IArtifactManager;
import dev.galasa.common.artifact.IBundleResources;
import dev.galasa.common.artifact.ISkeletonProcessor;
import dev.galasa.common.artifact.ISkeletonProcessor.SkeletonType;
import dev.galasa.common.artifact.SkeletonProcessorException;
import dev.galasa.framework.spi.AbstractManager;
import dev.galasa.framework.spi.AnnotatedField;
import dev.galasa.framework.spi.GenerateAnnotatedField;
import dev.galasa.framework.spi.IFramework;
import dev.galasa.framework.spi.IManager;
import dev.galasa.framework.spi.ResourceUnavailableException;

@Component(service = { IManager.class })
public class ArtifactManagerImpl extends AbstractManager implements IArtifactManager {

    private static final Log logger = LogFactory.getLog(ArtifactManagerImpl.class);

    @GenerateAnnotatedField(annotation=ArtifactManager.class)
	public IArtifactManager fillField(Field field, List<Annotation> annotations) {
		return this;
	}

	@Override
	public void provisionGenerate() throws ManagerException, ResourceUnavailableException {
		generateAnnotatedFields(ArtifactManagerField.class);
	}

	@Override
	public void provisionStop() {
		//Nothing is provisioned by this manager so we don't have anything to stop
	}

	@Override
	public IBundleResources getBundleResources(Class<?> owningClass) {
		return new BundleResourcesImpl(owningClass, this.getFramework());
	}

	@Override
	public ISkeletonProcessor getSkeletonProcessor() {
		return new VelocitySkeletonProcessor(this.getFramework());
	}

	@Override
	public ISkeletonProcessor getSkeletonProcessor(int skeletonType) throws SkeletonProcessorException {
		switch (skeletonType) {
			case SkeletonType.PLUSPLUS:
				return new PlusPlusSkeletonProcessor(this.getFramework());
			case SkeletonType.VELOCITY:
				return new VelocitySkeletonProcessor(this.getFramework());
			default:
				throw new SkeletonProcessorException("SkeletonType '" + skeletonType + "' is not a valid type");
		}
	}

	@Override
	public void initialise(@NotNull IFramework framework, @NotNull List<IManager> allManagers,
			@NotNull List<IManager> activeManagers, @NotNull Class<?> testClass) throws ManagerException {
		super.initialise(framework, allManagers, activeManagers, testClass);
		List<AnnotatedField> ourFields = findAnnotatedFields(ArtifactManagerField.class);
		if (!ourFields.isEmpty()) {
			youAreRequired(allManagers, activeManagers);
		}
	}

	@Override
	public void youAreRequired(@NotNull List<IManager> allManagers, @NotNull List<IManager> activeManagers)
			throws ManagerException {
		if (activeManagers.contains(this)) {
			return;
		}

		activeManagers.add(this);
	}
}