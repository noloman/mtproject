<?php

/* ScienceScienceAppBundle:Default:index.html.twig */
class __TwigTemplate_a2fae919121b32ee25ad51d2135e3ee4 extends Twig_Template
{
    protected function doDisplay(array $context, array $blocks = array())
    {
        $context = array_merge($this->env->getGlobals(), $context);

        // line 1
        echo "Hello ";
        echo twig_escape_filter($this->env, $this->getContext($context, 'name'), "html");
        echo "!
";
    }

    public function getTemplateName()
    {
        return "ScienceScienceAppBundle:Default:index.html.twig";
    }

    public function isTraitable()
    {
        return false;
    }
}
