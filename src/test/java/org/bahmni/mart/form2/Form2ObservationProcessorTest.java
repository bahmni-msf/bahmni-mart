package org.bahmni.mart.form2;

import org.bahmni.mart.form.domain.BahmniForm;
import org.bahmni.mart.form.domain.Concept;
import org.bahmni.mart.form.domain.Obs;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class Form2ObservationProcessorTest {

    @Test
    public void shouldSetFormAndReferencePathWhenPathDepthMatchesConfiguredDepth() throws Exception {
        Form2ObservationProcessor form2ObservationProcessor = new Form2ObservationProcessor();
        form2ObservationProcessor.setForm(createForm("FormOne", 3, 1));
        Obs obs = new Obs();

        invokeSetFormFieldPath(form2ObservationProcessor, obs, "FormOne.1/3-0/7-0/2-1");

        assertEquals("FormOne.1/3-0/7-0/2-1", obs.getFormFieldPath());
        assertEquals("FormOne.1/3-0", obs.getReferenceFormFieldPath());
    }

    @Test
    public void shouldFallbackToRootFormNameWhenLegacyTopLevelChildPathIsShallow() throws Exception {
        Form2ObservationProcessor form2ObservationProcessor = new Form2ObservationProcessor();
        BahmniForm form = createForm("FormOne", 1, 0);
        form.addField(new Concept(2, "FieldOne", 0));
        form.addField(new Concept(3, "FieldTwo", 0));
        form2ObservationProcessor.setForm(form);
        Obs obs = new Obs();

        invokeSetFormFieldPath(form2ObservationProcessor, obs, "FormOne.1/2-0");

        assertEquals("FormOne", obs.getFormFieldPath());
        assertEquals("FormOne", obs.getReferenceFormFieldPath());
    }

    @Test
    public void shouldKeepFullPathForSingleFieldTopLevelChildWhenPathIsShallow() throws Exception {
        Form2ObservationProcessor form2ObservationProcessor = new Form2ObservationProcessor();
        BahmniForm form = createForm("FormOne", 1, 0);
        form.addField(new Concept(2, "FieldOne", 0));
        form2ObservationProcessor.setForm(form);
        Obs obs = new Obs();

        invokeSetFormFieldPath(form2ObservationProcessor, obs, "FormOne.1/2-0");

        assertEquals("FormOne.1/2-0", obs.getFormFieldPath());
        assertEquals("FormOne", obs.getReferenceFormFieldPath());
    }

    @Test
    public void shouldTrimPathForTopLevelChildWhenPathHasExpectedDepth() throws Exception {
        Form2ObservationProcessor form2ObservationProcessor = new Form2ObservationProcessor();
        BahmniForm form = createForm("FormOne", 1, 0);
        form.addField(new Concept(2, "FieldOne", 0));
        form.addField(new Concept(3, "FieldTwo", 0));
        form2ObservationProcessor.setForm(form);
        Obs obs = new Obs();

        invokeSetFormFieldPath(form2ObservationProcessor, obs, "FormOne.1/2-0/3-0");

        assertEquals("FormOne.1/2-0", obs.getFormFieldPath());
        assertEquals("FormOne", obs.getReferenceFormFieldPath());
    }

    @Test
    public void shouldReturnRootFormNameForDepthZero() throws Exception {
        Form2ObservationProcessor form2ObservationProcessor = new Form2ObservationProcessor();
        String processedPath = invokeGetProcessedFormFieldPath(form2ObservationProcessor,
                "FormOne.1/3-0/7-0", 0);

        assertEquals("FormOne", processedPath);
    }

    private BahmniForm createForm(String rootFormName, int formDepthToParent, int parentDepthToParent) {
        BahmniForm rootForm = new BahmniForm();
        rootForm.setFormName(new Concept(1, rootFormName, 0));
        rootForm.setDepthToParent(0);

        BahmniForm parentForm = new BahmniForm();
        parentForm.setDepthToParent(parentDepthToParent);
        parentForm.setRootForm(rootForm);

        BahmniForm childForm = new BahmniForm();
        childForm.setDepthToParent(formDepthToParent);
        childForm.setParent(parentForm);
        childForm.setRootForm(rootForm);
        return childForm;
    }

    private void invokeSetFormFieldPath(Form2ObservationProcessor form2ObservationProcessor, Obs obs,
                                        String formFieldPath) throws Exception {
        Method setFormFieldPathMethod = Form2ObservationProcessor.class
                .getDeclaredMethod("setFormFieldPath", Obs.class, String.class);
        setFormFieldPathMethod.setAccessible(true);
        setFormFieldPathMethod.invoke(form2ObservationProcessor, obs, formFieldPath);
    }

    private String invokeGetProcessedFormFieldPath(Form2ObservationProcessor form2ObservationProcessor,
                                                   String formFieldPath, int depthToParent) throws Exception {
        Method getProcessedFormFieldPathMethod = Form2ObservationProcessor.class
                .getDeclaredMethod("getProcessedFormFieldPath", String.class, int.class);
        getProcessedFormFieldPathMethod.setAccessible(true);
        return (String) getProcessedFormFieldPathMethod
                .invoke(form2ObservationProcessor, formFieldPath, depthToParent);
    }
}
