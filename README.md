PenguinMenaceEngine
===================

The PenguinMenaceEngine general purpose 3d (game) engine.
It targets multiple platforms especially those having a penguin as a logo.

Motivation
==========

There is no good simulator engine for java.
We want to fill that gap and bring gaming and simulations closer to each other.

The engine is not only targeting those who create games, but also those who want to build a simulator or render images.
It is basically a simulator that is focused on realtime performance.
That makes it great for 3d gaming.
However, the PenguinMenaceEngine tries to reach for a larger target group than just gamedevelopers.

Setting up a project
====================

Download and isntall https://github.com/penguinmenac3/PenguinMenaceEngine-SDK.
Follow the instructions

The SDK will generate a maven project for you.

Exporting Blender Models
========================

Open your Model in Blender.

Open the export dialog at File->Eport->Wavefront(.obj)

Select 'Include Normals' on the left. 'Triangulate Faces' is optional when using quads and tris.

The Axis should be Forward: '-Z Forward' and Up:  'Y Up' by default.

Now chose a name for your obj and click 'Export OBJ'.

Usage
=====

Not too much information here yet.

In src/test/java there are classes to demonstrate how to initialize and use the game engine.

I hope that the rest is obvious from the JavaDoc, if not I will not blame you for asking me in a kind message. (I would also apreciate if you tell me what you think of the engine.)

Plans
=====

There is currently a next generation api in development. When the specification is done there will be created a design guide for new modules.
A current task is to unify the api design for easier use end extensibility.

OpenGL 3.2 and/or 4.2 coming. A modern OpenGL renderer is under development as well as a ui implementation.


Getting involved
================

You have an idea how to improve the gameengine or need a module for your own project? Implement it and write me an email about your module. I will then evaluate, if your idea fits into the ecosystem of the engine and merge it.

Write me an email: penguinmenac3 (a) gmail.com
