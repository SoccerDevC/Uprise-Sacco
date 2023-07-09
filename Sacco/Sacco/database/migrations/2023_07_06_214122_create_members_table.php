<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateMembersTable extends Migration
{
    public function up()
    {
        Schema::create('members', function (Blueprint $table) {
            $table->string('member_id');
            $table->integer('receipt_number');
            $table->string('name');
            $table->string('username');
            $table->string('password');
            $table->string('phone_number');
            
            $table->primary(['member_id', 'receipt_number']);
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('members');
    }
}
