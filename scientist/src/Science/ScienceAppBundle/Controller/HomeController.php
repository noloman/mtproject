<?php

namespace Science\ScienceAppBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;


class HomeController extends Controller
{
    
    public function indexAction()
    {
        return new Response("<h1>Welcome to the homepage</h1>");
    }
}
