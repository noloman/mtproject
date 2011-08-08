<?php

namespace Science\ScienceAppBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;


class DefaultController extends Controller
{
    
    public function indexAction($name)
    {
        return $this->render('ScienceScienceAppBundle:Default:index.html.twig', array('name' => $name));
    }
}
